package io.github.yangdaowan.surfear.core.config.converter;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author ycf
 **/
public class YamlConverter {

    /**
     * 将 Properties 转换为 YAML 格式字符串
     * @param properties Properties 对象
     * @return YAML 格式字符串
     */
    public static String propertiesToYaml(Properties properties) {
        if (properties == null || properties.isEmpty()) {
            return "";
        }

        // 将 Properties 转换为 Map 结构
        Map<String, Object> map = propertiesToMap(properties);

        // 配置 YAML 输出选项
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        // 创建 YAML 实例并转储
        Yaml yaml = new Yaml(options);
        return yaml.dump(map);
    }

    /**
     * 将 Properties 转换为嵌套的 Map 结构
     * @param properties Properties 对象
     * @return 嵌套的 Map 结构
     */
    private static Map<String, Object> propertiesToMap(Properties properties) {
        Map<String, Object> rootMap = new LinkedHashMap<>();

        properties.forEach((keyObj, valueObj) -> {
            String key = keyObj.toString();
            String value = valueObj.toString();

            // 按点号分割键名
            String[] keys = key.split("\\.");
            Map<String, Object> currentMap = rootMap;

            // 构建嵌套的 Map 结构
            for (int i = 0; i < keys.length - 1; i++) {
                String currentKey = keys[i];
                currentMap = (Map<String, Object>) currentMap.computeIfAbsent(
                        currentKey, k -> new LinkedHashMap<>());
            }

            // 设置最终的值
            String finalKey = keys[keys.length - 1];
            currentMap.put(finalKey, value);
        });

        return rootMap;
    }
}
