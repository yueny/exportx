package com.whosly.infra.exportx.bean;

import com.whosly.rapid.lang.mask.instance.AbstractMaskBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 全局配置
 *
 * 如果需要自定义配置，请使用 @Bean 的形式实例化
 */
@Builder
@AllArgsConstructor
public class GlobalConfigure extends AbstractMaskBo {
    /**
     * Automatic init by Spring content
     */
    @Getter
    private Boolean autoSpring;

    /**
     * 是否开启debug模式。开启后有更详细的日志输出。 默认不开启
     */
    @Getter
    private Boolean debug;

    /**
     * 大数据分批导入时，每个批次的查询数据量
     */
    @Getter
    @Builder.Default
    private Integer exportQuerySize = 2000;

    /**
     * 导出最大数量。默认 2W 最大值。允许自行设置，但务必慎重！
     */
    @Getter
    @Builder.Default
    private final Long limit = 20000L;

    //不允许自定义线程池
}
