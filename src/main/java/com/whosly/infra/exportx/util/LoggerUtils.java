package com.whosly.infra.exportx.util;

import com.whosly.infra.exportx.configuration.ExportConfigFactory;
import org.slf4j.Logger;

/**
 * 受限制的日志输出工具类
 */
public class LoggerUtils {

    public static void log(Logger log, String context, Object... param){
        if(ExportConfigFactory.getGlobalConfigure().getDebug()){
            log.info(context, param);
        }
    }
}
