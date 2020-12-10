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
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * excel Byte 流输出
 */
@Slf4j
public class ExcelByteExportor extends BaseExportor<ExcelWriter, WriteSheet> {

    public ExcelByteExportor(IExportConfig exportConfig){
        super(exportConfig, new StopWatch("ExcelExportor INIT"));
    }

    @Override
    protected void init(StopWatch watch) {
        super.init(watch);

        LoggerUtils.log(log, "ExcelByteExportor 初始化成功，耗时:{}.", watch.prettyPrint());
    }

    @Override
    public ExcelWriter buildWriter(IExportConfig exportConfig, DataHeads allDataHeads, DataHeads includeDataHeads, HttpServletResponse response) throws ExportxException {
        ExcelWriter writer = EasyExcel
                // 写入的输出流
                .write(new ByteArrayOutputStream())
                .head(exportConfig.getClazz())
                // 设置统一的表头、表内容样式
                .registerWriteHandler(horizontalCellStyleStrategy())
                .build();

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

            ByteArrayOutputStream outputStream = (ByteArrayOutputStream) getWriter().writeContext().writeWorkbookHolder().getOutputStream();

            return outputStream.toByteArray();
        }

        return null;
    }
}
