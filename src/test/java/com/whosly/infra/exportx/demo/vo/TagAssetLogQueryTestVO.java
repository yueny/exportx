package com.whosly.infra.exportx.demo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 模拟自定义查询条件。假定查询入参是此类。
 *
 * 仅做参考。不要copy
 */
@Getter
@Setter
public class TagAssetLogQueryTestVO { // extends AbstractQueryVO {

    /**
     * 查询页数
     */
    private Long page = 1L;

    /**
     * 每页大小
     */
    private Long rows = 15L;


    /**
     * 公司租户code
     */
    private String rentCode;

    /**
     * 标签key，数据库无此字段
     */
    private String tagKey;
}
