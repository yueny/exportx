package com.whosly.infra.exportx.annotation;

import com.whosly.infra.exportx.registrar.ExportsConfiguredRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 导出配置
 *
 * @author fengyang <deep_blue_yang@126.com>
 * @date 2020-11-10  15:54
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ExportsConfiguredRegistrar.class})
public @interface EnableExports {
    /**
     * 应用，用于区分不同系统的不同 uid 限制分离
     *
     * 待实现部分：
     *  默认读取 配置 spring.application.name。 读取不到则取默认值 "DEFAULT"
     */
    String applicationName() default "";

    /**
     * 执行的事件名
     */
    String[] action() default "";
}
