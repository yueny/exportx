package com.whosly.infra.exportx.exportor;

import com.whosly.infra.exportx.metadata.config.IExportConfig;
import com.whosly.infra.exportx.metadata.exportor.DataHeads;
import com.whosly.infra.exportx.metadata.config.ExportConfig;
import com.whosly.infra.exportx.exception.ExportxException;
import com.whosly.infra.exportx.metadata.api.IDataModel;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * csv 导出, new init， muti instance
 *
 * 自行实现
 */
@Deprecated
public class CSVExportor extends BaseExportor<String, String> {
    public CSVExportor(ExportConfig exportConfig){
        super(exportConfig, new StopWatch("CSVExportor INIT"));
        // 未实现
    }

    @Override
    public String buildWriter(IExportConfig exportConfig, DataHeads allDataHeads, DataHeads includeDataHeads, HttpServletResponse response) throws ExportxException {
        return null;
    }

    @Override
    public String buildTarget(IExportConfig exportConfig) {
        return null;
    }

    @Override
    public String getWriter() {
        return null;
    }

    @Override
    public String getTarget() {
        return null;
    }

    @Override
    public IExportConfig getExportConfig() {
        return null;
    }

    @Override
    public void write(List<? extends IDataModel> list) throws ExportxException {

    }

    @Override
    public Object finishAndGet() {
        return null;
    }

    @Override
    public void finish() {

    }
}
