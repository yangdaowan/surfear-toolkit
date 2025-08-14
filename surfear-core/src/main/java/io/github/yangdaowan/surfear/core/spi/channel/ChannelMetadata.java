package io.github.yangdaowan.surfear.core.spi.channel;

import io.github.yangdaowan.surfear.core.exception.MessageChannelException;
import io.github.yangdaowan.surfear.core.spi.client.Config;
import io.github.yangdaowan.surfear.core.spi.client.ConfigPrefix;
import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;
import lombok.Getter;

/**
 * 通道元数据接口 - 描述通道的基本信息
 */
@Getter
public class ChannelMetadata {

    private final MessageType type;
    private final String name;
    private final String description;
    private final Class<? extends Message> model;
    private final Class<? extends MessageChannelException> exception;
    private final Class<? extends Config> config;

    public ChannelMetadata(Channel annotation) {
        this.type = annotation.type();
        this.name = annotation.name();
        this.description = annotation.description();
        this.model = annotation.model();
        this.config = annotation.config();
        this.exception = annotation.exception();
    }

    /**
     * 获取完整通道标识
     */
    public String getChannelKey() {
        return this.type.name().toLowerCase() + ":" + this.name.toLowerCase();
    }

    /**
     * 获取配置前缀
     * 如果配置类上有 @ConfigPrefix 注解，则返回注解中的值加上点号，否则返回 null
     *
     * @return 配置前缀字符串或 null
     */
    public String getConfigPrefix() {
        if(this.getConfig().isAnnotationPresent(ConfigPrefix.class)){
            ConfigPrefix configPrefix = this.getConfig().getAnnotation(ConfigPrefix.class);
            return configPrefix.value() + ".";
        }
        return null;
    }

}
