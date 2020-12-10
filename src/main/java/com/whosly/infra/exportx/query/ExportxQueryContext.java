package com.whosly.infra.exportx.query;

import com.whosly.rapid.lang.mask.instance.AbstractMaskBo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExportxQueryContext extends AbstractMaskBo {

    /**
     * 查询页数
     */
    @Builder.Default
    private Integer pageNo = 1;

    /**
     * 每页大小
     */
    @Builder.Default
    private final Integer pageSize = 100;

    public Integer nextPage(){
        this.pageNo = this.pageNo + 1;

        return this.pageNo;
    }
}
