package com.whosly.infra.exportx.exportor.wrapper;

import com.whosly.infra.exportx.exception.ExportxException;
import com.whosly.infra.exportx.query.api.DataService;
import com.whosly.infra.exportx.query.api.IConditions;

import java.util.concurrent.ExecutorService;

/**
 *
 * @param <O> 写入操作对象， 如 ExcelWriter
 * @param <T> 写入的目标对象 tagregt， 如   WriteSheet
 */
public interface IWrapper<O, T> {
    /**
     * 数据加载
     */
    Object loader(DataService dataService, ExecutorService executorService) throws ExportxException;

    /**
     * 数据加载
     */
    Object loader(DataService dataService, ExecutorService executorService, IConditions conditions) throws ExportxException;

}
