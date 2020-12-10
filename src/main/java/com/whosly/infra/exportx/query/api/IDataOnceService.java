package com.whosly.infra.exportx.query.api;

import com.whosly.infra.exportx.metadata.api.IDataModel;

import java.util.List;

/**
 * 一次性的数据抽取服务
 */
public interface IDataOnceService<C> extends DataService {

    /**
     * 一次性获取处理数据.
     *
     * @return 待处理的数据集合
     */
    List<? extends IDataModel> getData(IConditions<C> conditions);
}
