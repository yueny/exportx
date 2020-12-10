package com.whosly.infra.exportx.query.api;

import com.whosly.infra.exportx.query.ExportxQueryContext;
import com.whosly.infra.exportx.metadata.api.IDataModel;
import org.springframework.data.domain.Page;

/**
 * 分批获取数据的抽取服务
 */
public interface IDataBatchService<C> extends DataService {

    /**
     * 分页获取待处理数据.
     *
     * @param context 分片上下文
     * @return 待处理的数据集合
     */
    Page<? extends IDataModel> fetchData(ExportxQueryContext context, IConditions<C> conditions);
}
