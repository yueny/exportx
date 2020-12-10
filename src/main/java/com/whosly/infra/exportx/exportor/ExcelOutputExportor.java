package com.whosly.infra.exportx.exportor;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.whosly.infra.exportx.exception.ExportxException;
import com.whosly.infra.exportx.metadata.api.IDataModel;
import com.whosly.infra.exportx.metadata.config.IExportConfig;
import com.whosly.infra.exportx.metadata.exportor.DataHeads;
import com.whosly.infra.exportx.util.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * excel 文件输出流
 */
@Slf4j
public class ExcelOutputExportor extends BaseExportor<ExcelWriter, WriteSheet> {
    private HttpServletResponse response;

    public ExcelOutputExportor(HttpServletResponse response, IExportConfig exportConfig) {
        super(exportConfig, new StopWatch("ExcelOutputExportor INIT"));

        this.response = response;
    }

    @Override
    protected void init(StopWatch watch) {
        super.init(watch);

        LoggerUtils.log(log,  "ExcelOutputExportor 初始化成功，耗时:{}.", watch.prettyPrint());
    }


    @Override
    public ExcelWriter buildWriter(IExportConfig exportConfig, DataHeads allDataHeads, DataHeads includeDataHeads, HttpServletResponse response) throws ExportxException {
        String fileName =  exportConfig.getFileName();

        ExcelWriter writer = EasyExcel
                .write(getHttpOutputStream(fileName, response))
                .head(exportConfig.getClazz())
                .registerWriteHandler(horizontalCellStyleStrategy())
                .build();

        return writer;
    }

    @Override
    public WriteSheet buildTarget(IExportConfig exportConfig) {
        WriteSheet writeSheet = EasyExcel.writerSheet(exportConfig.getFileName())
                .autoTrim(true)
                .build();

        return writeSheet;
    }

    @Override
    public void write(List<? extends IDataModel> list) throws ExportxException {
        getWriter().write(list, getTarget());
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

            try {
                response.flushBuffer();
            } catch (Exception e) {
                log.error("response 模式捯饬文件失败, e:", e);
            }
        }

        return null;
    }


    /**
     * 导出文件时为 Writer 生成 OutputStream
     *
     * @param fileName
     * @param response
     * @return
     */
    private static OutputStream getHttpOutputStream(String fileName, HttpServletResponse response) throws ExportxException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");

            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            throw new ExportxException("导出excel表格失败!", e);
        }
    }

}
