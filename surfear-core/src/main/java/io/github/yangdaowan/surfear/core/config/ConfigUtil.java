package io.github.yangdaowan.surfear.core.config;

import io.github.yangdaowan.surfear.core.config.converter.ConfigConverter;
import io.github.yangdaowan.surfear.core.exception.ConfigException;
import io.github.yangdaowan.surfear.core.spi.channel.ChannelMetadata;
import io.github.yangdaowan.surfear.core.spi.client.Config;
import io.github.yangdaowan.surfear.core.spi.client.ConfigPrefix;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author ycf
 **/
public class ConfigUtil {

    /**
     * 获取当前通道，包含所有层级的新配置对象
     */
    public static Properties getChannelConfig(Properties config){
        Properties properties = ConfigurationManager.getConfigProperties();
        if(config != null){
            properties.putAll(config);
        }
        return properties;
    }

    /**
     * 从通道中获取默认配置
     */
    public static Properties getChannelDefaultConfig(ChannelMetadata metadata){
        try {
            Class<?> clazz = metadata.getConfig();
            if(clazz.equals(Config.class)){
                return null;
            }

            String prefix = metadata.getConfigPrefix();

            Object object = clazz.newInstance();
            return ConfigConverter.convertToProperties(object, prefix);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ConfigException("配置对象反射失败", e);
        }
    }

    /**
     * 获取当前通道配置对象
     * @param config 临时的通道配置，参数可以传入null，表示从默认配置文件中获取
     * @param clazz 配置对象的类型
     */
    public static <T> T getChannelConfigObject(Properties config, Class<T> clazz){
        Properties properties = getChannelConfig(config);
        ConfigPrefix configPrefix = clazz.getAnnotation(ConfigPrefix.class);
        if(configPrefix == null){
            throw new ConfigException("配置对象反射失败，缺失 ConfigPrefix 注解：" + clazz.getName());
        }

        String prefix = configPrefix.value();
        T configObj = ConfigConverter.convertToObject(properties, clazz, prefix + ".");

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass.isAnnotationPresent(ConfigPrefix.class)) {
            // 获取父类配置对象
            Object parentConfig = getChannelConfigObject(config, superClass);
            // 将父类的字段值复制到当前对象
            copyParentFields(configObj, parentConfig, superClass);
        }

        return configObj;
    }

    /**
     * 递归复制父类的字段（包括私有字段）
     */
    private static void copyParentFields(Object child, Object parent, Class<?> parentClass) {
        if (child == null || parent == null || parentClass == null) {
            return;
        }

        // 复制当前父类的字段
        for (Field parentField : parentClass.getDeclaredFields()) {
            try {
                parentField.setAccessible(true);
                Object value = parentField.get(parent);
                if (value != null) {
                    // 在子类中查找同名字段（包括继承的字段）
                    Field childField = findFieldInHierarchy(child.getClass(), parentField.getName());
                    if (childField != null) {
                        childField.setAccessible(true);
                        childField.set(child, value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new ConfigException("配置对象反射失败" , e);
            }
        }

        // 递归处理更上层的父类
        Class<?> grandParentClass = parentClass.getSuperclass();
        if (grandParentClass != null && grandParentClass.isAnnotationPresent(ConfigPrefix.class)) {
            copyParentFields(child, parent, grandParentClass);
        }
    }

    /**
     * 在类继承链中查找字段（包括父类私有字段）
     */
    private static Field findFieldInHierarchy(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName); // 先查当前类
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return findFieldInHierarchy(superClass, fieldName); // 递归查父类
            }
            return null;
        }
    }
}
