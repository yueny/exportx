package com.whosly.infra.exportx.registry;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 由于此处实例为  spring 管理的对象，因此不存在内存泄漏问题
 */
public class ObjectRegistry {
    private static ObjectRegistry instance = new ObjectRegistry();

    public static ObjectRegistry getInstance() {
        return instance;
    }

    /**
     * 应用名
     */
    @Getter
    private String applicationName;

    private ConcurrentHashMap<String, Object> properties;

    private ObjectRegistry() {
        if (properties == null) {
            properties = new ConcurrentHashMap<>();
        }
    }

    public synchronized void set(String name) {
        if(StringUtils.isEmpty(name)){
            name = "DEFAULT";
        }

        this.applicationName = name;
    }

    public synchronized void register(String name, Object object) {
        if (properties == null) {
            properties = new ConcurrentHashMap<>();
        }

        if(properties.contains(name)){
            return;
        }

        properties.put(name, object);
    }

    public void register(Class<?> object) {
        register(object.getClass().getName(), object);
    }

    public synchronized void unregister(String name) {
        if(!properties.contains(name)){
            return;
        }

        properties.remove(name);
    }

    public void unregister(Class<?> type) {
        unregister(type.getName());
    }

    public Object lookup(String name) {
        return properties != null ? properties.get(name) : null;
    }

    public <T> T lookup(Class<T> type) {
        Object object = lookup(type.getName());
        return type.isInstance(object) ? type.cast(object) : null;
    }

}
