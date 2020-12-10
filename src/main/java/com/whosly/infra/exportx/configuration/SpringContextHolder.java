package com.whosly.infra.exportx.configuration;

import com.whosly.infra.exportx.metadata.config.Configurable;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring init， single instance
 */
public class SpringContextHolder implements ApplicationContextAware, Configurable {
    private SpringContextHolder(){
        //.
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext get() {
        assertApplicationContext();

        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) get().getBean(beanName);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return get().getBean(requiredType);
    }

    private static void assertApplicationContext() {
        if (SpringContextHolder.applicationContext == null) {
            throw new RuntimeException("applicaitonContext  属性为null,请检查是否注入了 SpringContextHolder!");
        }
    }
}
