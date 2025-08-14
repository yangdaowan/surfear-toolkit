package io.github.yangdaowan.surfear.core.config;

import io.github.yangdaowan.surfear.core.config.converter.ConfigConverter;
import io.github.yangdaowan.surfear.core.exception.ConfigException;
import io.github.yangdaowan.surfear.core.spi.channel.ChannelMetadata;
import io.github.yangdaowan.surfear.core.util.MessageConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.MapConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 配置管理，配置项快照<br/>
 * default < 项目文件 < 环境变量 < 系统属性 < 临时覆盖。
 */
@Slf4j
public class ConfigurationManager {

    // 通道默认配置
    private final PropertiesConfiguration defaultConfig = new PropertiesConfiguration();
    // 固定配置快照加载
    private final CompositeConfiguration configuration = createConfiguration();
    //
    private final Properties properties = toFilteredProperties();

    public static Properties getConfigProperties() {
        return Holder.INSTANCE.getProperties();
    }

    public static CompositeConfiguration getConfig() {
        return Holder.INSTANCE.getConfiguration();
    }

    public static void putDefaultConfig(ChannelMetadata metadata) {
        Holder.INSTANCE.addDefaultConfig(metadata);
    }


    public static CompositeConfiguration createConfig(Properties temporaryOverrides) {
        return Holder.INSTANCE.createConfiguration(temporaryOverrides);
    }

    public Properties toFilteredProperties(){
        return ConfigConverter.toFilteredProperties(configuration, MessageConstant.base);
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * 从多个通道加载默认配置
     */
    public void addDefaultConfig(ChannelMetadata metadata) {
        Properties props = ConfigUtil.getChannelDefaultConfig(metadata);
        if(props != null && !props.isEmpty()) {
            props.forEach((k, v) -> {
                if(v != null && !"".equals(v)) {
                    defaultConfig.setProperty((String)k, (String)v);
                }
            });
        }
    }

    /**
     * 创建项目配置构建器(优先.properties，其次.yaml)
     */
    private CompositeConfiguration getProjectConfig() {
        // Try both standard resource locations
        String[] configDirs = {
                "src/main/resources",  // main resources
                "src/test/resources",  // test resources
                "resources",           // current directory resources
                ""                     // root directory
        };

        CompositeConfiguration config = new CompositeConfiguration();

        for (String configDir : configDirs) {
            File propertiesFile = Paths.get(configDir, MessageConstant.base + ".properties").toFile();
            File yamlFile = Paths.get(configDir, MessageConstant.base + ".yaml").toFile();

            // 首先尝试属性文件
            if (propertiesFile.exists()) {
                try {
                    PropertiesConfiguration pc = new PropertiesConfiguration();
                    pc.setListDelimiterHandler(new DefaultListDelimiterHandler(','));
                    try (InputStreamReader reader = new InputStreamReader(
                            Files.newInputStream(propertiesFile.toPath()), StandardCharsets.UTF_8)) {
                        pc.read(reader);
                    }
                    config.addConfiguration(pc);
                } catch (Exception e) {
                    throw new ConfigException("项目配置文件加载错误："+propertiesFile.getName(), e);
                }
            }

            // 然后尝试 yaml 文件
            if (yamlFile.exists()) {
                try {
                    YAMLConfiguration yc = new YAMLConfiguration();
                    yc.setThrowExceptionOnMissing(true);
                    try (InputStreamReader reader = new InputStreamReader(
                            Files.newInputStream(yamlFile.toPath()), StandardCharsets.UTF_8)) {
                        yc.read(reader);
                    }
                    config.addConfiguration(yc);
                    break;
                } catch (Exception e) {
                    throw new ConfigException( "项目配置文件加载错误："+yamlFile.getName(), e);
                }
            }
        }

        if (config.getNumberOfConfigurations() == 0) {
            log.warn("未找到项目配置文件: {} 或 {} 在任何标准位置",
                    MessageConstant.base + ".properties",
                    MessageConstant.base + ".yaml");
        }
        return config;
    }

    /**
     * 创建最新的配置变量快照
     * @return 包含所有固定的配置项对象
     */
    public CompositeConfiguration createConfiguration() {
        CompositeConfiguration config = new CompositeConfiguration();

        // 按优先级从低到高添加配置
        config.addConfiguration(defaultConfig);                              // 默认配置
        config.addConfiguration(getProjectConfig());                         // 项目文件配置
        config.addConfiguration(new LowerCaseEnvironmentConfiguration());    // 操作系统环境变量

        return config;
    }

    /**
     * 创建包含临时覆盖的配置对象
     * @param temporaryOverrides 临时配置覆盖
     * @return 包含所有层级的新配置对象
     */
    public CompositeConfiguration createConfiguration(Properties temporaryOverrides) {
        CompositeConfiguration config = new CompositeConfiguration(configuration);
        if(temporaryOverrides != null) {
            config.addConfiguration(new MapConfiguration(temporaryOverrides));   // 临时配置(最高)
        }
        return config;
    }

    /**
     * 获取固定的配置项快照
     * @return
     */
    public CompositeConfiguration getConfiguration() {
        return configuration;
    }

    private static class Holder {
        static final ConfigurationManager INSTANCE = new ConfigurationManager();
    }

}