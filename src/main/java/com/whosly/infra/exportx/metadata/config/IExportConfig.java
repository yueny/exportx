package com.whosly.infra.exportx.metadata.config;

import com.whosly.infra.exportx.enums.ExportType;
import com.whosly.infra.exportx.metadata.api.IDataModel;

public interface IExportConfig {

    /**
     * 导出的文件名称所处的路径，默认 /tmp
     */
    String getOutputPath();

    /**
     * 导出的文件名称。 暂时包含自定义后缀。
     *
     * 如果是 excel, 默认 sheetName 也为 fileName。
     */
    String getFileName();

    /**
     * 导出类型。默认 csv
     */
    ExportType getExportType();

    /**
     * 导出数据的数据类型 bean, 如  UserVO.class
     */
    Class<? extends IDataModel> getClazz();

    /**
     * 当前允许的导出最大数量。当大于全局限制的时候，以全局限制为准； 当小于全局限制时， 以本配置为准。
     *
     * 当配置不存在时，以全局限制为准。
     */
    Long getLimit();

    /**
     * 导出的文件全路径，包含后缀名
     *
     * 如 '/tmp/b/c/ddd/    aaaa   .xlsx'
     */
    default String getFilePath(){
        return getOutputPath() + "/" + getFileName() + getExportType().getFileType();
    }
}
