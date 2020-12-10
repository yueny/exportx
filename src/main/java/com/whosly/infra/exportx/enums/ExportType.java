package com.whosly.infra.exportx.enums;

import lombok.Getter;

public enum ExportType {

    /**
     * 说明:
     *      .xls 格式的excel(最大行数65536行,最大列数256列)
     *  	.xlsx 格式的excel(最大行数1048576行,最大列数16384列)
     */
    EXCEL2007(".xlsx", "application/vnd.ms-excel"),

    CSV(".csv", "application/csv"),

    /**
     * byte[] 流输出
     */
    BYTE("", "application/csv"),

    /**
     * output 输出
     */
    OUT_PUT("", "application/output");

    @Getter
    private final String fileType;
    @Getter
    private final String contentType;

    ExportType(String fileType, String contentType) {
        this.fileType = fileType;
        this.contentType = contentType;
    }

}
