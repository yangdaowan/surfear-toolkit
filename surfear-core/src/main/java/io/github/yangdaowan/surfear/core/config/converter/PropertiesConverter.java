package io.github.yangdaowan.surfear.core.config.converter;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.Properties;

/**
 * @author ycf
 **/
public class PropertiesConverter {

    /**
     * 将 YAML 字符串转换为 Properties 对象
     *
     * @param yamlStr YAML 格式字符串
     * @return Properties 对象
     */
    public static Properties yamlToProperties(String yamlStr) {
        Properties properties = new Properties();
        if (yamlStr == null || yamlStr.trim().isEmpty()) {
            return properties;
        }

        // 解析 YAML
        Yaml yaml = new Yaml();
        Map<String, Object> yamlMap = yaml.load(yamlStr);

        // 将嵌套的 Map 结构转换为 Properties
        if (yamlMap != null) {
            flattenMap("", yamlMap, properties);
        }

        return properties;
    }

    /**
     * 递归展平嵌套的 Map 结构
     *
     * @param prefix     当前前缀
     * @param map        当前层级的 Map
     * @param properties 目标 Properties 对象
     */
    private static void flattenMap(String prefix, Map<String, Object> map, Properties properties) {
        map.forEach((key, value) -> {
            String fullKey = prefix.isEmpty() ? key : prefix + "." + key;

            if (value instanceof Map) {
                // 递归处理嵌套的 Map
                flattenMap(fullKey, (Map<String, Object>) value, properties);
            } else if (value != null) {
                // 将值放入 Properties
                properties.setProperty(fullKey, value.toString());
            }
        });
    }


}
