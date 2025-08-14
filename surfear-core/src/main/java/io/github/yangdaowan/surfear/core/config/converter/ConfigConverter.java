package io.github.yangdaowan.surfear.core.config.converter;

import io.github.yangdaowan.surfear.core.exception.ConfigException;
import io.github.yangdaowan.surfear.core.spi.MessageChannelRegistry;
import io.github.yangdaowan.surfear.core.spi.channel.ChannelMetadata;
import io.github.yangdaowan.surfear.core.spi.client.Config;
import io.github.yangdaowan.surfear.core.util.MessageConstant;
import org.apache.commons.configuration2.CompositeConfiguration;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * @author ycf
 **/
public class ConfigConverter {

    // 默认的字段名转换器（驼峰转点分隔）
    public static final Function<String, String> DEFAULT_NAME_CONVERTER = fieldName -> {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (c == '_') {
                sb.append('.');
//            } else if (Character.isUpperCase(c)) {
//                sb.append('.').append(Character.toLowerCase(c));
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    };

    // 默认的值转换器（直接调用toString）
    public static final Function<Object, String> DEFAULT_VALUE_CONVERTER = v -> {
        if (v == null) {
            return "";
        }
        return v.toString();
    };

    /**
     * 将 CompositeConfiguration 转换为 Properties，并过滤指定前缀的 key
     * @param config  CompositeConfiguration 实例
     * @param prefix  需要保留的 key 前缀（如 "db."）
     * @return 过滤后的 Properties
     */
    public static Properties toFilteredProperties(CompositeConfiguration config, String prefix) {
        Properties props = new Properties();
        Iterator<String> keys = config.getKeys(); // 获取所有 key

        while (keys.hasNext()) {
            String key = keys.next();
            if (key.startsWith(prefix)) { // 过滤前缀
                Object value = config.getProperty(key);
                props.put(key, value);
            }
        }
        return props;
    }

    /**
     * 将对象转换为 Properties 格式
     * @param config 配置对象
     * @param prefix 属性前缀
     * @param nameConverter 字段名转换函数
     * @param valueConverter 值转换函数
     * @return Properties 对象
     */
    public static Properties convertToProperties(Object config,
                                                 String prefix,
                                                 Function<String, String> nameConverter,
                                                 Function<Object, String> valueConverter) {
        Properties props = new Properties();
        if (config == null) {
            return props;
        }

        Class<?> clazz = config.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(config);
                String propertyName = prefix + nameConverter.apply(field.getName());
                String propertyValue = valueConverter.apply(value);
                props.setProperty(propertyName, propertyValue);
            } catch (IllegalAccessException ignored) {
                throw new ConfigException("配置对象反射失败："+ ignored.getMessage());
            }
        }

        return props;
    }

    // 简化版转换方法
    public static Properties convertToPropertiesOfChannelKey(Object config, String channelKey) {
        String prefix = MessageChannelRegistry.getMetadata(channelKey).getConfigPrefix();
        return convertToProperties(config, prefix);
    }

    // 简化版转换方法
    public static Properties convertToProperties(Object config, String prefix) {
        return convertToProperties(config, prefix, DEFAULT_NAME_CONVERTER, DEFAULT_VALUE_CONVERTER);
    }

    // 简化版转换方法
    public static Properties convertToProperties(String channelKey) throws InstantiationException, IllegalAccessException {
        ChannelMetadata metadata = MessageChannelRegistry.getMetadata(channelKey);
        String prefix = metadata.getConfigPrefix();
        Class<? extends Config> clazz = metadata.getConfig();
        Object object = clazz.newInstance();

        return ConfigConverter.convertToProperties(object, MessageConstant.base + "." + prefix);
    }

    /**
     * 将 Properties 对象格式化为字符串
     * @param props Properties 对象
     * @return 格式化的字符串
     */
    public static String formatProperties(Properties props) {
        StringBuilder sb = new StringBuilder();
        props.forEach((key, value) -> {
            sb.append(key).append("=").append(value).append("\n");
        });
        return sb.toString();
    }

    /**
     * 将 Properties 对象格式化为环境变量字符串
     * @param channelKey 通道key
     * @return 格式化的字符串
     */
    public static String formatEnvironmentVariable(String channelKey) throws InstantiationException, IllegalAccessException {
        Properties config = convertToProperties(channelKey);
        return ConfigConverter.formatEnvironmentVariable(config);
    }

    /**
     * 将 Properties 对象格式化为环境变量字符串
     * @param props Properties 对象
     * @return 格式化的字符串
     */
    public static String formatEnvironmentVariable(Properties props) {
        if (props == null || props.isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner("\n");

        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            // 转换key为环境变量格式：替换.为_，转换为大写
            String envKey = key.replace('.', '_').toUpperCase();

            joiner.add(envKey + "=" + value + ";");
        }

        return joiner.toString();
    }

    /**
     * 将 Properties 转换为 Java 对象
     * @param props Properties 对象
     * @param clazz 目标类
     * @param prefix 属性前缀(需要去除的部分)
     * @param <T> 目标类型
     * @return 转换后的 Java 对象
     * @throws Exception 转换异常
     */
    public static <T> T convertToObject(Properties props, Class<T> clazz, String prefix){
        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();

                // 构建属性名：prefix + 转换后的字段名
                String propertyName = MessageConstant.base + "."
                        + (prefix != null && !prefix.isEmpty() ? prefix : "")
                        + DEFAULT_NAME_CONVERTER.apply(fieldName);

                if(props.containsKey(propertyName)){
                    Object value = props.get(propertyName);
                    if (value == null) {
                        throw new RuntimeException(clazz.getSimpleName() + "." + fieldName + " 映射的配置字段 " + propertyName + " 不能为空");
                    }

                    if (value instanceof String) {
                        setFieldValue(obj, field, value.toString()); // 假设需要特殊处理
                    } else {
                        field.set(obj, value); // 其他类型直接赋值
                    }
                } else {
                    // 如果属性不存在，检查对象是否存在默认值，否则抛出异常
                    if(field.get(obj) == null){
                        throw new RuntimeException(clazz.getSimpleName() + "." + fieldName + " 映射的配置字段 " + propertyName + " 不能为空");
                    }
                }
            }
            return obj;
        } catch (Exception e) {
            throw new ConfigException("配置对象反射失败：" + e.getMessage(), e);
        }
    }

    /**
     * 设置字段值
     * @param obj 目标对象
     * @param field 字段
     * @param value 字符串值
     * @throws IllegalAccessException 访问异常
     */
    private static void setFieldValue(Object obj, Field field, String value) throws IllegalAccessException {
        Class<?> type = field.getType();

        if (type == String.class) {
            field.set(obj, value);
        } else if (type == int.class || type == Integer.class) {
            field.set(obj, Integer.parseInt(value));
        } else if (type == long.class || type == Long.class) {
            field.set(obj, Long.parseLong(value));
        } else if (type == boolean.class || type == Boolean.class) {
            field.set(obj, Boolean.parseBoolean(value));
        } else if (type == double.class || type == Double.class) {
            field.set(obj, Double.parseDouble(value));
        } else if (type == float.class || type == Float.class) {
            field.set(obj, Float.parseFloat(value));
        } else {
            // 其他类型可以在这里扩展
            field.set(obj, value);
        }
    }



}
