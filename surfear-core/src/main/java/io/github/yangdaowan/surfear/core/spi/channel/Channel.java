package io.github.yangdaowan.surfear.core.spi.channel;

import io.github.yangdaowan.surfear.core.exception.MessageChannelException;
import io.github.yangdaowan.surfear.core.spi.client.Config;
import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通道扫描注解
 *
 * @author ycf
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Channel {
    /**
     * 消息类型
     */
    MessageType type();

    /**
     * 通道名称
     */
    String name();

    /**
     * 通道描述
     */
    String description();

    /**
     * 消息模型
     */
    Class<? extends Message> model();

    /**
     * 配置模型
     */
    Class<? extends Config> config() default Config.class;

    /**
     * 异常类型
     */
    Class<? extends MessageChannelException> exception();

}
