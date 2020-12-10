package com.whosly.infra.exportx.bean;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.whosly.infra.exportx.metadata.config.Configurable;
import com.whosly.rapid.lang.mask.instance.AbstractMaskBo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ExportSpringAtomticConfigure extends AbstractMaskBo implements InitializingBean, Configurable {
    /**
     * 限制下载数。 < 0: 未配置， 不生效
     */
    @Value("${atomtic.limit.key:-1}")
    @Getter
    private Long limit;

    /**
     * FORID 限制下载数。 < 0: 未配置， 不生效
     */
    private Map<String, Long> limitForId = new HashMap<>();

//    public Long byForId(String id) {
//        return limitForId.getOrDefault(id, -1L);
//    }

    /**
     * 配置的namespace， 允许自定义， 如 application
     *
     * 配置的namespace 为 FX.app 的原因在于，不希望什么东西都放在 application 中，但允许自行设置为 application
     */
    @Setter
    private String namespace = "FX.app";

    /**
     * 全局限制下载数
     */
    private static final String ATOMTIC_LIMIT_KEY = "atomtic.limit.key";

    /**
     * 制定具体导出 id 的限制下载数. atomtic.limit.key.[id]
     */
    private static final String ATOMTIC_FORID_LIMIT_KEY = "atomtic.limit.key.";

    private static Config nsConfig = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 未被 Spring 加载
        if(limit == null) {
            limit = -2L;

            load(false);
            return;
        }

        // 被 Spring 加载，但未读到配置
        if(limit == -1){
            load(true);
            return;
        }
    }

    public boolean load(boolean autoSpring) {
        try {
            long startTime = System.currentTimeMillis();
            Config config = ConfigService.getConfig(namespace);
            nsConfig = config;

            findStatus();

            long endTime = System.currentTimeMillis();
            log.info("SpringAtomtic 配置初始化完成。 耗时:{} ms。", (endTime - startTime));
        } catch (Exception e) {
            log.error("SpringAtomtic findStatus 出现异常！", e);
        }

        try {
            holdStatus();
            return true;
        } catch (Exception e) {
            log.error("SpringAtomtic holdStatus 出现异常！", e);
        }
        return false;
    }

    private void findStatus() {
        finds(nsConfig.getProperty(ATOMTIC_LIMIT_KEY, ""));
    }

    private void holdStatus() {
        nsConfig.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);

                    log.info("ExportSpringAtomticConfigure Found change - key: {}, oldValue: {}, newValue: {}, changeType: {}",
                            change.getPropertyName(), change.getOldValue(), change.getNewValue(),
                            change.getChangeType());

                    if (StringUtils.equals(change.getPropertyName(), ATOMTIC_LIMIT_KEY)) {
                        finds(change.getNewValue());
                    }

                    if (StringUtils.startsWith(change.getPropertyName(), ATOMTIC_FORID_LIMIT_KEY)) {
                        try {
                            String id = StringUtils.substringAfterLast(change.getPropertyName(), ATOMTIC_FORID_LIMIT_KEY);

                            limitForId.put(id, Long.parseLong(change.getNewValue()));
                        } catch (Exception e) {
                            log.error("导出配置 格式转换异常：", e);
                        }
                    }
                }
            }
        });
    }

    private void finds(String newValue) {
        log.debug("finds newValue: {}", newValue);

        if (StringUtils.isNotEmpty(newValue)) {
            try {
                limit = Long.parseLong(newValue);
            } catch (Exception e) {
                log.error("导出配置 格式转换异常：", e);
            }
        } else {
            //.
        }
    }
}
