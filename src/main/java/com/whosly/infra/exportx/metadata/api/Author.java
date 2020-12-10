package com.whosly.infra.exportx.metadata.api;

import com.whosly.rapid.lang.mask.instance.AbstractMaskBo;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;

@Builder
@Getter
public class Author extends AbstractMaskBo implements Serializable {
    /**
     * 业务标识的 用户 ID， 视各个系统特征而定，可以为手机号码，可以为身份证，可以为 uid, 但在每个应用层级必须维度一致。。
     *
     * 改值会作为限制改 用户 的导出限制条件
     */
    @NonNull
    private String id;

    /**
     * 用户 name
     */
    private String name;

}
