package com.whosly.infra.exportx.exportor;

import com.whosly.infra.exportx.enums.ExportType;
import com.whosly.infra.exportx.metadata.config.IExportConfig;
import com.whosly.infra.exportx.metadata.exportor.DataHeads;
import com.whosly.infra.exportx.exception.ExportxException;
import com.whosly.infra.exportx.metadata.api.IDataModel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IExportor<O, T> {
    /**
     * 新构造 写入的操作对象
     *
     * @param exportConfig
     */
    O buildWriter(IExportConfig exportConfig, DataHeads allDataHeads, DataHeads includeDataHeads, HttpServletResponse response) throws ExportxException;

    /**
     * 新构造 写入的目标对象
     *
     * @param exportConfig
     */
    T buildTarget(IExportConfig exportConfig);

    /**
     * 得到写入的操作对象
     */
    O getWriter();

    /**
     * 得到 写入的目标对象
     */
    T getTarget();

    /**
     * 得到 exportConfig
     */
    IExportConfig getExportConfig();

    /**
     * 得到 ExportType
     */
    ExportType getExportType();

    /**
     * 将数据 list 通过 writer 写入 target 中
     *
     * @param list
     * @throws ExportxException
     */
    void write(List<? extends IDataModel> list) throws ExportxException;

    /**
     * Close IO 并 取到输出对象
     */
    Object finishAndGet();
}
