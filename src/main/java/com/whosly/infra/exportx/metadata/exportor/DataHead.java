package com.whosly.infra.exportx.metadata.exportor;

import com.whosly.rapid.lang.mask.instance.AbstractMaskBo;
import lombok.Builder;
import lombok.Getter;

/**
 * 数据与导出的格式对应
 *
 */
@Getter
@Builder
public class DataHead extends AbstractMaskBo {
    /**
     * 数据库字段或者实体 field name
     */
    private String column;

    /**
     * 导出的标题中文。 为空则取  column
     */
    private String alias;

}
