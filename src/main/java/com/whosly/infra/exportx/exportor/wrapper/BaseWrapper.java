package com.whosly.infra.exportx.exportor.wrapper;

import com.alibaba.excel.annotation.ExcelProperty;
import com.whosly.infra.exportx.configuration.ExportConfigFactory;
import com.whosly.infra.exportx.metadata.FieldData;
import com.whosly.infra.exportx.exception.ExportxException;
import com.whosly.infra.exportx.exportor.IExportor;
import com.whosly.infra.exportx.metadata.config.IExportConfig;
import com.whosly.infra.exportx.metadata.exportor.DataHead;
import com.whosly.infra.exportx.metadata.exportor.DataHeads;
import com.whosly.infra.exportx.query.ExportxQueryContext;
import com.whosly.infra.exportx.query.api.DataService;
import com.whosly.infra.exportx.query.api.IConditions;
import com.whosly.infra.exportx.query.api.IDataBatchService;
import com.whosly.infra.exportx.query.api.IDataOnceService;
import com.whosly.infra.exportx.metadata.api.IDataModel;
import com.whosly.infra.exportx.util.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.util.StopWatch;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class BaseWrapper<O, T> implements IWrapper<O, T> {
//    /**
//     * 实体最终的映射字段哈希集合
//     *
//     * class name : columns, heads
//     *
//     */
//    private static Map<String, List<FieldData>> FINAL_FIELD_MAPS = new ConcurrentHashMap<>();

    private AtomicLong longAdder;

    private IExportor<O, T> exportor = null;
    public BaseWrapper(IExportor exportor) {
        this.exportor = exportor;

        this.longAdder = new AtomicLong(0L);
    }

    public IExportor<O, T> getExportor() {
        if(exportor == null){
            throw new ExportxException("不支持的导出操作类型：exportor 为null.");
        }

        return exportor;
    }

    @Override
    public Object loader(DataService dataService, ExecutorService executorService) throws ExportxException {
        return loader(dataService, executorService, (IConditions) null);
    }

    @Override
    public final Object loader(DataService dataService, ExecutorService executorService, IConditions conditions) throws ExportxException {
        StopWatch watch = new StopWatch("export_wrapper");

        boolean rs = writer(dataService, executorService, conditions, watch);

        log.info("数据导出完成:{}， 总计导出数据 {} 条「/{}」.", rs, longAdder.get(), exportor.getExportConfig().getLimit());
        LoggerUtils.log(log, "耗时:{}.", watch.prettyPrint());

        if(!rs){
            return null;
        }
        return writerAfterGet();
    }

    /**
     * 将数据写入  目标对象中
     */
    private boolean writer(DataService dataService, ExecutorService executorService, IConditions conditions, StopWatch watch) {
        if(dataService instanceof IDataOnceService){
            IDataOnceService dataOnceService = (IDataOnceService) dataService;

            watch.start("dataOnce_GetData");
            List<? extends IDataModel> list = dataOnceService.getData(conditions);
            watch.stop();

            watch.start("dataOnce_ExportData");
            exportData(list);
            watch.stop();
        }else {
            IDataBatchService dataBatchService = (IDataBatchService) dataService;

            ExportxQueryContext queryContext = ExportxQueryContext.builder()
                    .pageSize(ExportConfigFactory.getGlobalConfigure().getExportQuerySize())
                    .build();
            fetchData(dataBatchService, queryContext, executorService, conditions, 1, watch);
        }

        return true;
    }

    /**
     * 写入完成后的输出
     */
    private Object writerAfterGet() {
        return getExportor().finishAndGet();
    }

    private boolean fetchData(IDataBatchService dataBatchService, ExportxQueryContext queryContext,
                              ExecutorService executorService, IConditions conditions, int deep, StopWatch watch) {
        // 第一页查询
        watch.start("dataBatch_GetData_" + queryContext.getPageNo());
        Page<? extends IDataModel> listPage = dataBatchService.fetchData(queryContext, conditions);
        watch.stop();

        if(listPage == null || CollectionUtils.isEmpty(listPage.getContent())){
            return false;
        }

        // 有输出数据， 先导出
        watch.start("dataBatch_ExportData_" + queryContext.getPageNo());
        boolean rs = exportData(listPage.getContent());
        watch.stop();

        /* 同步操作 */
        // 输出未受限制 true，且有下一页。
        while(rs && listPage.hasNext()){
            queryContext.nextPage();

            rs = fetchData(dataBatchService, queryContext, executorService, conditions, deep + 1, watch);
        }
        return rs;

//        /* 异步操作 */
//        // 第二页开始直至最后一页的多线程操作
//        // 输出受限制 false，或者没有下一页。都结束
//        if(!rs || !listPage.hasNext()){
//            return rs;
//        }
//
//        // 总页数： listPage.getTotalPages()
//        for (int i = 2; i< listPage.getTotalPages(); i++){
//            queryContext.nextPage();
//
//            executorService.submit(() -> {
//                fetchData(dataBatchService, queryContext, executorService, conditions, deep + 1, watch);
//            });
//        }
//        return rs;
    }

    private boolean exportData(List<? extends IDataModel> list) {
        if(CollectionUtils.isEmpty(list)){
            LoggerUtils.log(log, "导出数据为空:{}，导出结束.", list.size());

            return false;
        }

        // 此处做导出总数限制
        Long currenCount = longAdder.get();
        Long limit = exportor.getExportConfig().getLimit();
        if(currenCount >= limit){
            LoggerUtils.log(log, "导出数据超出总线制， currenCount:{}, limit:{}，导出结束.", currenCount, limit);

            return false;
        }

        // 取剩余的容量
        Long allowCount = Math.min(limit - currenCount, list.size());

        if(allowCount.longValue() == list.size()){
            LoggerUtils.log(log, "导出数据正常，该次查询出的数据 {} 条导出中.", list.size());

            getExportor().write(list);
            longAdder.addAndGet(list.size());

            return true;
        }

        // 截取 list。 输出受限， 之后的不再查询
        List<? extends IDataModel> allowList = list.subList(0, allowCount.intValue());

        getExportor().write(allowList);
        currenCount = longAdder.addAndGet(allowList.size());

        LoggerUtils.log(log, "导出数据受限，部分导出， 此次导出数:{}， 总计导出数:{}, limit:{}，导出结束.",
                allowList.size(), currenCount, limit);

        return false;
    }


    /**
     * ####################################################
     * ###########    static  method  #####################
     * #####################################################
     */
    public static Map.Entry<DataHeads, DataHeads> getDataHeads(IExportConfig exportConfig) throws ExportxException {
        // all
        List<FieldData> allFields = getAllExportxField(exportConfig);

        List<String> columnsAllConf = allFields.stream().map(fieldData -> fieldData.getColumn()).collect(Collectors.toList());
        List<String> aliasAllConf = allFields.stream().map(fieldData -> fieldData.getAlias()).collect(Collectors.toList());
        List<String> fieldNameAllConf = allFields.stream().map(fieldData -> fieldData.getFieldName()).collect(Collectors.toList());

        // include
        List<FieldData> includeColumnFileds = new ArrayList<FieldData>(Arrays.asList(new FieldData[allFields.size()]));
        Collections.copy(includeColumnFileds, allFields);

        return new AbstractMap.SimpleEntry(assembly(allFields), assembly(includeColumnFileds));
    }

    private static DataHeads assembly(List<FieldData> list) {
        if(CollectionUtils.isEmpty(list)){
            return null;
        }

        List<DataHead> dataHeadList = new ArrayList<>();
        for (FieldData fieldData : list) {
            String column = fieldData.getColumn();
            String head = fieldData.getAlias();

            dataHeadList.add(DataHead.builder().column(column).alias(head).build());
        }

        DataHeads dataHeads = DataHeads.builder()
                .dataHeads(dataHeadList)
                .build();

        return dataHeads;
    }

    private static List<FieldData> getAllExportxField(IExportConfig exportConfig) {
        String clazzName = exportConfig.getClazz().getSimpleName();

        List<FieldData> fieldMaps;
//        if(FINAL_FIELD_MAPS.containsKey(clazzName)){
//            fieldMaps = FINAL_FIELD_MAPS.get(clazzName);
//        }else {
            fieldMaps = getAllExportxField(exportConfig.getClazz());
//            FINAL_FIELD_MAPS.put(clazzName, fieldMaps);
//        }

        return Collections.unmodifiableList(fieldMaps);
    }

    private static List<FieldData> getAllExportxField(Class<? extends IDataModel> clazz){
        // 获取所有字段
        Field[] fields = clazz.getDeclaredFields();

        // 是否拥有 ExcelProperty 注解。 拥有则返回空。
        boolean hasExcelAnno = false;
        for(Field field : fields) {
            ExcelProperty excelProperty = getAnnotationPresent(field, ExcelProperty.class);

            if(excelProperty != null){
                hasExcelAnno = true;
                break;
            }
        }
        if(hasExcelAnno){
            return Collections.emptyList();
        }

        List<FieldData> fieldDataList = new ArrayList<>(fields.length);
        // 排序
        List<Field> fieldList = Arrays.asList(fields);

        for(Field field : fieldList) {
                fieldDataList.add(FieldData.builder()
                        .alias(field.getName())
                        .column(field.getName())
                        .fieldName(field.getName())
                        .build());
        }

        return fieldDataList;
    }

    private static <T extends Annotation> T getAnnotationPresent(Field field, Class<T> annotationClass) {
        // 判断这个字段是否有 注解
        if(field.isAnnotationPresent(annotationClass)){
            T annotation = field.getAnnotation(annotationClass);

            return annotation;
        }

        return null;
    }

}
