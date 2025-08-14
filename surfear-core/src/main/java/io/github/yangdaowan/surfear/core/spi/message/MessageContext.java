package io.github.yangdaowan.surfear.core.spi.message;

import lombok.Getter;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

/**
 * 消息上下文
 *
 * @author ycf
 */
@Getter
public class MessageContext implements Serializable {

    /**
     * 获取消息id - 每条消息唯一
     */
    private final String messageId ;
    /**
     * 消息通道业务名称
     */
    private final String channelKey;
    /**
     * 消息模型
     */
    private final Message model;
    /**
     * 通道配置
     */
    private final Properties config;
    /**
     * 开始时间
     */
    private final long startTime;

    public MessageContext(String channelKey, Message model) {
        this(channelKey, model, null);
    }

    public MessageContext(String channelKey, Message model, Properties config) {
        this.messageId = UUID.randomUUID().toString();
        this.channelKey = channelKey;
        this.model = model;
        this.config = config;
        this.startTime = System.currentTimeMillis();

    }

}
