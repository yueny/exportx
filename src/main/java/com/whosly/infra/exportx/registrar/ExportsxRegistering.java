package com.whosly.infra.exportx.registrar;

import com.whosly.infra.exportx.configuration.ExportConfigFactory;
import com.whosly.infra.exportx.configuration.ExportxThreadPoolConfig;
import com.whosly.infra.exportx.configuration.SpringContextHolder;
import com.whosly.infra.exportx.registry.ObjectRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Import(value = {ExportxThreadPoolConfig.class,
        SpringContextHolder.class,
        ExportConfigFactory.class})
public class ExportsxRegistering implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware {
    private static ApplicationContext applicationContext;

    private final AnnotationValueHolder valueHolder;

    ExportsxRegistering(AnnotationValueHolder valueHolder) {
        this.valueHolder = valueHolder;

        afterConstruct();
    }

    private void afterConstruct() {
        log.debug("1 afterConstruct and register action!");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("3 afterPropertiesSet!");

        if(valueHolder != null){
            String srtringApplicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
            String applicationName = applicationContext.getEnvironment().getProperty("application.name");

            ObjectRegistry.getInstance().set(valueHolder.getApplicationName());

            if(valueHolder.getActions() != null && valueHolder.getActions().length > 0){
                List<String> actions = Arrays.asList(valueHolder.getActions());

                actions.stream().map(String::trim)
                        // 过滤掉空串
                        .filter(action -> !action.isEmpty())
                        .forEach(action -> {
                            ObjectRegistry.getInstance().register(action, this);
                        });
            }
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.debug("4 postProcessBeanDefinitionRegistry");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.debug("5 postProcessBeanFactory");
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        log.debug("2 setApplicationContext!");

        ExportsxRegistering.applicationContext = context;
    }
}
