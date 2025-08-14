package io.github.yangdaowan.surfear.core.util;

import io.github.yangdaowan.surfear.core.concurrent.ExecutorFactory;
import io.github.yangdaowan.surfear.core.exception.MessageException;
import io.github.yangdaowan.surfear.core.interceptor.InterceptorHandler;
import io.github.yangdaowan.surfear.core.spi.MessageChannelRegistry;
import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * 消息发送器 - 统一的消息发送入口
 * <p>
 * 提供统一的消息发送接口，支持多种通道类型的消息发送。
 * 通过通道键(channelKey)来路由到具体的消息通道实现。
 * 支持同步和异步两种发送方式。
 * </p>
 * 
 * <h3>使用示例：</h3>
 * <pre>{@code
 * // 同步发送
 * MessageResult result = MessageSender.send("mail:smtp", emailModel);
 * 
 * // 异步发送
 * CompletableFuture<MessageResult> future = MessageSender.sendAsync("sms:aliyun", smsModel);
 * 
 * // 使用临时配置
 * Properties config = new Properties();
 * config.put("surfear.smtp-mail.host", "smtp.example.com");
 * MessageResult result = MessageSender.send("mail:smtp", emailModel, config);
 * }</pre>
 * 
 * @author ycf
 * @see MessageChannel
 * @see MessageResult
 * @see Message
 */
public class MessageSender {

    /**
     * 消息推送统一入口
     * @param channelKey 通道key
     * @param model 消息模型
     * @return 消息结果
     * @throws MessageException 消息异常
     */
    public static MessageResult send(String channelKey, Message model) throws MessageException {
        return send(channelKey, model, null);
    }

    /**
     * 消息异步推送简化方法
     * @param channelKey 通道key
     * @param model 消息模型
     * @return CompletableFuture<MessageResult> 异步结果
     * @throws MessageException 消息异常
     */
    public static CompletableFuture<MessageResult> sendAsync(String channelKey, Message model) throws MessageException {
        return sendAsync(channelKey, model, null);
    }

    /**
     * 消息推送统一入口
     * @param channelKey 通道key
     * @param model 消息模型
     * @param config 通道配置，优先率最高，一次性
     * @return 消息结果
     * @throws MessageException 消息异常
     */
    public static MessageResult send(String channelKey, Message model, Properties config) throws MessageException {
        // 创建消息上下文
        MessageContext context = new MessageContext(channelKey, model, config);
        // 从通道注册中心获取指定通道
        MessageChannel channel = MessageChannelRegistry.getChannel(context.getChannelKey(), config);
        // 获取拦截器链路处理器
        InterceptorHandler handler = MessageChannelRegistry.getInterceptorHandler(channel);
        // 拦截器链路处理
        return handler.handler(context);
    }

    /**
     * 消息推送统一入口，异步
     * @param channelKey 通道key
     * @param model 消息模型
     * @param config 通道配置，优先率最高，一次性
     * @return CompletableFuture<MessageResult> 异步结果
     * @throws MessageException 消息异常
     */
    public static CompletableFuture<MessageResult> sendAsync(String channelKey, Message model, Properties config) throws MessageException {
        // 创建消息上下文
        MessageContext context = new MessageContext(channelKey, model, config);
        // 从通道注册中心获取指定通道
        MessageChannel channel = MessageChannelRegistry.getChannel(context.getChannelKey(), config);
        // 获取拦截器链路处理器
        InterceptorHandler handler = MessageChannelRegistry.getInterceptorHandler(channel);

        return CompletableFuture.supplyAsync(() -> {
            // 在异步线程中执行拦截器链和消息发送
            return handler.handler(context);
        }, ExecutorFactory.getDefaultExecutor());
    }

}
