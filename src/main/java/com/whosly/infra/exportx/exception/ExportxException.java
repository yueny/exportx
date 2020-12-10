package com.whosly.infra.exportx.exception;

import lombok.Getter;
import lombok.Setter;

public class ExportxException extends RuntimeException {

    /**
     * 错误码
     */
    @Getter
    @Setter
    private String code;

    /**
     * 错误描述
     */
    @Getter
    @Setter
    private String message;

    public ExportxException(String message) {
        super(message);
        this.code = "-1";
        this.message = message;
    }

    public ExportxException(Integer code, String message) {
        super(message);
        this.code = code.toString();
        this.message = message;
    }

    public ExportxException(String message, Throwable e) {
        super(message, e);
        this.code = "-1";
        this.message = message;
    }

    public ExportxException(Integer code, String message, Throwable e) {
        super(message, e);
        this.code = code.toString();
        this.message = message;
    }

}
