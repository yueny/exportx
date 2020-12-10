package com.whosly.infra.exportx.registrar;

import com.whosly.infra.exportx.annotation.EnableExports;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * spring init， single instance
 */
public class ExportsConfiguredRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String BEAN_NAME = "exportsConfiguredRegistrar";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationValueHolder holder = getAnnotationVal(importingClassMetadata);
        if (registry.containsBeanDefinition(BEAN_NAME)) {
            updatePostProcessor(registry, holder);
        } else {
            addPostProcessor(registry, holder);
        }
    }

    private AnnotationValueHolder getAnnotationVal(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(EnableExports.class.getName()));

        String applicationName = attributes.getString("applicationName");
        String[] actions = attributes.getStringArray("action");

        AnnotationValueHolder holder = AnnotationValueHolder.builder()
                .applicationName(applicationName)
                .actions(actions)
                .build();

        return holder;
    }

    private void updatePostProcessor(BeanDefinitionRegistry registry, AnnotationValueHolder holder) {
        BeanDefinition definition = registry.getBeanDefinition(BEAN_NAME);
        ConstructorArgumentValues.ValueHolder constructorArguments = definition.getConstructorArgumentValues()
                .getGenericArgumentValue(AnnotationValueHolder.class);

        constructorArguments.setValue(holder);
    }

    private void addPostProcessor(BeanDefinitionRegistry registry, AnnotationValueHolder holder) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ExportsxRegistering.class);
        beanDefinition.getConstructorArgumentValues()
                .addGenericArgumentValue(holder);

        beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registry.registerBeanDefinition(BEAN_NAME, beanDefinition);
    }
}
