package com.whosly.infra.exportx;

import com.whosly.infra.exportx.configuration.ExportConfigFactory;
import com.whosly.infra.exportx.exportor.CSVExportor;
import com.whosly.infra.exportx.exportor.ExcelByteExportor;
import com.whosly.infra.exportx.exportor.ExcelExportor;
import com.whosly.infra.exportx.exportor.ExcelOutputExportor;
import com.whosly.infra.exportx.exportor.wrapper.Wrappers;
import com.whosly.infra.exportx.exportor.wrapper.IWrapper;
import com.whosly.infra.exportx.metadata.api.Author;
import com.whosly.infra.exportx.metadata.config.ExportConfig;
import com.whosly.infra.exportx.query.api.DataService;
import com.whosly.infra.exportx.enums.ExportType;
import com.whosly.infra.exportx.exception.ExportxException;
import com.whosly.infra.exportx.query.api.IConditions;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.concurrent.ExecutorService;

public class Exportors {
    /**
     * @param dataService  数据筛选服务
     * @param exportConfig  导出的文件配置
     * @return File
     */
    @Deprecated
    public static File export(DataService dataService, ExportConfig exportConfig)
            throws ExportxException {
        return export(dataService, exportConfig, (Author) null);
    }

    /**
     * @param dataService  数据筛选服务
     * @param exportConfig  导出的文件配置
     * @return File
     */
    public static File export(DataService dataService, ExportConfig exportConfig, Author author)
            throws ExportxException {
        return export(dataService, exportConfig, author, null);
    }

    /**
     * @param dataService  数据筛选服务
     * @param exportConfig  导出的文件配置
     * @return File
     */
    @Deprecated
    public static File export(DataService dataService, ExportConfig exportConfig, IConditions conditions)
            throws ExportxException {
        return export(dataService, exportConfig, null, conditions);
    }

    /**
     * @param dataService  数据筛选服务
     * @param exportConfig  导出的文件配置
     * @return File
     */
    public static File export(DataService dataService, ExportConfig exportConfig, Author author, IConditions conditions)
            throws ExportxException {
        ExecutorService executorService = ExportConfigFactory.getExecutorService();

        IWrapper wrapper = getWrapper(exportConfig, null);

        // author 和 conditions 均允许为null
        return (File) wrapper.loader(dataService, executorService, conditions);
    }

    /**
     * @param dataService  数据筛选服务
     * @param exportConfig  导出的文件配置
     * @return byte[]  byte[]
     */
    @Deprecated
    public static byte[] exportByte(DataService dataService, ExportConfig exportConfig)
            throws Exception {
        return exportByte(dataService, exportConfig, (Author) null);
    }

    /**
     * @param dataService  数据筛选服务
     * @param exportConfig  导出的文件配置
     * @return byte[]  byte[]
     */
    public static byte[] exportByte(DataService dataService, ExportConfig exportConfig, Author author)
            throws Exception {
        return exportByte(dataService, exportConfig, author, null);
    }

    /**
     * @param dataService  数据筛选服务
     * @param exportConfig  导出的文件配置
     * @return byte[]  byte[]
     */
    @Deprecated
    public static byte[] exportByte(DataService dataService, ExportConfig exportConfig, IConditions conditions)
            throws Exception {
        return exportByte(dataService, exportConfig, null, conditions);
    }

    /**
     * @param dataService  数据筛选服务
     * @param exportConfig  导出的文件配置
     * @return byte[]  byte[]
     */
    public static byte[] exportByte(DataService dataService, ExportConfig exportConfig, Author author, IConditions conditions)
            throws Exception {
        exportConfig.setExportType(ExportType.BYTE);

        ExecutorService executorService = ExportConfigFactory.getExecutorService();

        IWrapper wrapper = getWrapper(exportConfig, null);

        // author 和 conditions 均允许为null
        byte[] bytes = (byte[]) wrapper.loader(dataService, executorService, conditions);

        return bytes;

//        // 生成文件
//        File file = export(dataService, exportConfig, author, conditions);
//
//        // 读文件
//        byte[] bytes = null;
//        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
//            FileInputStream in = new FileInputStream(file)) {
//            byte[] b = new byte[1024];
//
//            int i = 0;
//            while ((i = in.read(b)) != -1) {
//                out.write(b, 0, b.length);
//            }
//
//            bytes = out.toByteArray();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 删除文件
//        Files.delete(file.toPath());
//
//        return bytes;
    }



    /**
     * @param dataService  数据筛选服务
     * @param exportConfig  导出的文件配置
     */
    public static void exportOutputStream(HttpServletResponse response, DataService dataService, ExportConfig exportConfig, Author author)
            throws ExportxException {
        exportOutputStream(response, dataService, exportConfig, null, null);
    }

    /**
     * @param dataService  数据筛选服务
     * @param exportConfig  导出的文件配置
     */
    public static void exportOutputStream(HttpServletResponse response, DataService dataService, ExportConfig exportConfig, IConditions conditions, Author author)
            throws ExportxException {
        exportConfig.setExportType(ExportType.OUT_PUT);

        ExecutorService executorService = ExportConfigFactory.getExecutorService();

        IWrapper wrapper = getWrapper(exportConfig, response);

        wrapper.loader(dataService, executorService, conditions);
    }

    /**
     * @param exportConfig  导出的文件配置
     * @return
     */
    private static IWrapper getWrapper(ExportConfig exportConfig, HttpServletResponse response)
            throws ExportxException {
        ExportConfigFactory.assemblyConfig(exportConfig);

        ExportType exportType = exportConfig.getExportType();

        IWrapper wrapper;
        if(exportType == ExportType.EXCEL2007) {
            wrapper = new Wrappers(new ExcelExportor(exportConfig));
        }else if(exportType == ExportType.CSV) {
            wrapper = new Wrappers(new CSVExportor(exportConfig));
        }else if(exportType == ExportType.BYTE) {
            wrapper = new Wrappers(new ExcelByteExportor(exportConfig));
        }else if(exportType == ExportType.OUT_PUT) {
            wrapper = new Wrappers(new ExcelOutputExportor(response, exportConfig));
        } else {
            throw new ExportxException("不支持的导出操作：" + exportType);
        }

        return wrapper;
    }

}
