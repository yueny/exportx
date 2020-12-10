package com.whosly.infra.exportx.registrar;


import com.whosly.rapid.lang.mask.instance.AbstractMaskBo;
import lombok.*;

/**
 * 注解的配置参数持有者
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnotationValueHolder extends AbstractMaskBo {
    /**
     * 应用名，用于区分不同系统的不同 uid 限制分离
     */
    @NonNull
    @Getter
    private String applicationName;

    /**
     * 执行的事件名
     */
    @NonNull
    @Getter
    private String[] actions;

}
