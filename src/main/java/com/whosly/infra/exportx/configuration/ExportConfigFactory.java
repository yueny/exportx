package com.whosly.infra.exportx.configuration;

import com.whosly.infra.exportx.bean.ExportSpringAtomticConfigure;
import com.whosly.infra.exportx.metadata.config.Configurable;
import com.whosly.infra.exportx.bean.GlobalConfigure;
import com.whosly.infra.exportx.metadata.config.ExportConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ExecutorService;

/**
 * spring init， single instance
 */
@Slf4j
public class ExportConfigFactory implements Configurable, InitializingBean {

    /**
     * 全局静态配置
     */
    private static GlobalConfigure globalConfigure;

    /**
     * Spring 动态配置
     */
    private static ExportSpringAtomticConfigure exportSpringAtomticConfigure;

    private static Object LOCKER = new Object();

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            GlobalConfigure globalConfigureBean = SpringContextHolder.getBean(GlobalConfigure.class);

            ExportConfigFactory.globalConfigure = GlobalConfigure.builder()
                    .autoSpring(globalConfigureBean.getAutoSpring() != null ? globalConfigureBean.getAutoSpring() : true)
                    .debug(globalConfigureBean.getDebug() != null ? globalConfigureBean.getDebug() : false)
                    .exportQuerySize(globalConfigureBean.getExportQuerySize())
                    .limit(globalConfigureBean.getLimit())
                    .build();
        } catch (Exception e){
            GlobalConfigure gc = getDefaultGlobalConfigure();

            ExportConfigFactory.globalConfigure = gc;
        }
        log.info("ExportConfigFactory globalConfigure :{}.", globalConfigure);

        try{
            ExportSpringAtomticConfigure sac = SpringContextHolder.getBean(ExportSpringAtomticConfigure.class);
            ExportConfigFactory.exportSpringAtomticConfigure = sac;
        } catch (Exception e){
            ExportSpringAtomticConfigure sac = new ExportSpringAtomticConfigure();
            sac.afterPropertiesSet();

            ExportConfigFactory.exportSpringAtomticConfigure = sac;
        }
        log.info("ExportConfigFactory exportSpringAtomticConfigure :{}.", exportSpringAtomticConfigure);
    }

    /**
     * 获取导出的线程池对象
     *
     * @return
     */
    public static GlobalConfigure getGlobalConfigure() {
        if(globalConfigure == null){
            synchronized (LOCKER){
                if(globalConfigure == null){
                    globalConfigure = getDefaultGlobalConfigure();
                }
            }
        }

        return globalConfigure;
    }

    /**
     * 获取导出的线程池对象
     *
     * @return
     */
    public static ExecutorService getExecutorService() {
        return SpringContextHolder.getBean(ExportxThreadPoolConfig.NAME);
    }

    private static GlobalConfigure getDefaultGlobalConfigure() {
        // 其余采用默认值
        return GlobalConfigure.builder()
                .autoSpring(false)
                .debug(false)
                .build();
    }

    /**
     * 结合 全局配置、配置中心配置，局部配置，得出当前的有效配置
     * @param exportConfig
     */
    public static void assemblyConfig(ExportConfig exportConfig){
        // 获取全局配置
        Long globalLimitConfig = getGlobalConfigure().getLimit();

        // 获取配置中心配置
        Long atomticLimitConfig = exportSpringAtomticConfigure.getLimit();
        if(atomticLimitConfig == null || atomticLimitConfig < 0){
            atomticLimitConfig = globalLimitConfig;
        }

        // 当配置中心与代码中全局配置不一致时，取 最小值
        Long actulGlobalLimit= Math.min(atomticLimitConfig, globalLimitConfig);

        if(exportConfig.getLimit() == null ||
                exportConfig.getLimit() > actulGlobalLimit.longValue()) {
            exportConfig.setLimit(actulGlobalLimit);
        }
    }
}
