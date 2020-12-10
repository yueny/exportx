package com.whosly.infra.exportx.query.api;

public interface IConditions<C> {

    /**
     * 自定义扩展的查询条件。 默认不实现返回 null.
     *
     * @return
     */
    default C getCondition(){
        return null;
    }
}
