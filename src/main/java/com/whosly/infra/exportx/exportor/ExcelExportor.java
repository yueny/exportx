package com.whosly.infra.exportx.exportor;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.whosly.infra.exportx.metadata.config.IExportConfig;
import com.whosly.infra.exportx.metadata.exportor.DataHead;
import com.whosly.infra.exportx.metadata.exportor.DataHeads;
import com.whosly.infra.exportx.exception.ExportxException;
import com.whosly.infra.exportx.metadata.api.IDataModel;
import com.whosly.infra.exportx.util.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * excel 文件 导出， new init， muti instance
 */
@Slf4j
public class ExcelExportor extends BaseExportor<ExcelWriter, WriteSheet> {
    public ExcelExportor(IExportConfig exportConfig){
        super(exportConfig, new StopWatch("ExcelExportor INIT"));
    }

    @Override
    protected void init(StopWatch watch) {
        super.init(watch);

        LoggerUtils.log(log,  "ExcelExportor 初始化成功，耗时:{}.", watch.prettyPrint());
    }

    @Override
    public ExcelWriter buildWriter(IExportConfig exportConfig, DataHeads allDataHeads, DataHeads includeDataHeads, HttpServletResponse response) throws ExportxException {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());

        // 单元格策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

        // 字体大小 及 字体样式 add by  zhuli2-ac@aicaigroup.com
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteFont.setFontName("宋体");
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        // 初始化表格样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle,
                contentWriteCellStyle);


        StringBuffer title = new StringBuffer();
        title.append(DateUtils.format(new Date(),"yyyy年MM月dd日"));
        title.append(exportConfig.getFileName());

        String fileName =  exportConfig.getFileName();

        ExcelWriter writer = null;
        if(allDataHeads != null){
            writer = EasyExcel
                    .write(exportConfig.getOutputPath() + "/" + fileName)
                    .head(getHead(title.toString(), allDataHeads))
                    // 自定义导出的列
                    .includeColumnFiledNames(includeDataHeads.headMap().getKey())
                    // 设置统一的表头、表内容样式
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .build();
        }else{
            writer = EasyExcel
                    .write(exportConfig.getOutputPath() + "/" + fileName)
                    .head(exportConfig.getClazz())
                    // 设置统一的表头、表内容样式
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .build();
        }

        return writer;
    }

    @Override
    public WriteSheet buildTarget(IExportConfig exportConfig) {
        WriteSheet writeSheet = EasyExcel.writerSheet(exportConfig.getFileName())
                .autoTrim(true)
//                .sheetNo(1)
//                .needHead(true)
                .build();

        return writeSheet;
    }

    @Override
    public void write(List<? extends IDataModel> list) throws ExportxException {
        // QueryWriter
        getWriter().write(list, getTarget());
    }

    /**
     * 导出文件头
     */
    private List<List<String>> getHead(String title, DataHeads dataHeads){
        List<List<String>> head = new ArrayList<>();

        for (DataHead dataHead : dataHeads.getDataHeads()) {
            List<String> headColumn = new ArrayList<>();
            headColumn.add(title);
            headColumn.add(dataHead.getAlias());

            head.add(headColumn);
        }

        return head;
    }

    @Override
    public void finish() {
        if(this.getWriter() != null){
            this.getWriter().finish();
        }
    }

    @Override
    public Object finishAndGet() {
        if(this.getWriter() != null){
            finish();

            String fileName =  getExportConfig().getFileName();

            String filePath = getExportConfig().getOutputPath() + "/" + fileName;
            File file = new File(filePath);

            // 返回文件且文件不删除
            return file;
        }

        return null;
    }
}
