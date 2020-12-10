package com.whosly.infra.exportx.metadata;

import com.whosly.rapid.lang.mask.instance.AbstractMaskBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldData extends AbstractMaskBo {

    /**
     * 数据库列名。
     */
    @Deprecated
    private String column;

    /**
     * 导出的表头别名。
     */
    private String alias;

    /**
     * field 字段名
     */
    private String fieldName;
}
