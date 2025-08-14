package io.github.yangdaowan.surfear.core.spi;

import io.github.yangdaowan.surfear.core.config.ConfigurationManager;
import io.github.yangdaowan.surfear.core.exception.ChannelLoadException;
import io.github.yangdaowan.surfear.core.exception.MessageException;
import io.github.yangdaowan.surfear.core.interceptor.InterceptorHandler;
import io.github.yangdaowan.surfear.core.interceptor.MessageInterceptor;
import io.github.yangdaowan.surfear.core.spi.channel.ChannelMetadata;
import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 消息通道注册中心
 * <p>
 * 负责管理所有消息通道和拦截器的注册、加载和获取。
 * 基于Java SPI机制自动发现和加载消息通道实现。
 * 在类加载时自动初始化所有可用的通道和拦截器。
 * </p>
 * 
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>自动发现并注册消息通道</li>
 *   <li>管理拦截器链</li>
 *   <li>提供通道元数据查询</li>
 *   <li>配置默认参数</li>
 * </ul>
 * 
 * <h3>SPI配置：</h3>
 * <p>消息通道需要在META-INF/services/io.github.yangdaowan.surfear.core.spi.channel.MessageChannel文件中注册</p>
 * <p>拦截器需要在META-INF/services/io.github.yangdaowan.surfear.core.interceptor.MessageInterceptor文件中注册</p>
 * 
 * @author ycf
 * @see MessageChannel
 * @see MessageInterceptor
 * @see java.util.ServiceLoader
 */
@Slf4j
public class MessageChannelRegistry {

    private static final Map<String, MessageChannel> channels = new HashMap<>();
    private static final List<MessageInterceptor> interceptorList = new ArrayList<>();

    static {
        try {
            // 基于SPI加载所有通道
            initMessageChannel();

            // 基于SPI加载所有拦截器
            initMessageInterceptor();

        } catch (Exception e) {
            throw new ChannelLoadException("初始化失败！", e);
        }
    }


    /**
     * 初始化消息通道
     * <p>
     * 使用ServiceLoader加载所有实现了MessageChannel接口的类，
     * 注册到通道映射中，并设置默认配置。
     * </p>
     * 
     * @throws MessageException 如果发现重复的通道键
     */
    public static void initMessageChannel(){
        ServiceLoader<MessageChannel> services = ServiceLoader.load(MessageChannel.class);
        log.info("当前消息通道清单：");
        log.info("--------------------------------------");
        for (MessageChannel channel : services) {

            ChannelMetadata metadata = channel.getMetadata();
            String channelKey = metadata.getChannelKey();
            String description = metadata.getDescription();
            // 添加通道
            if(channels.containsKey(channelKey)){
                throw new MessageException("重复的消息通道：" + channelKey);
            }
            channels.put(channelKey, channel);
            // 从通道中获取默认配置
            ConfigurationManager.putDefaultConfig(metadata);
            log.info("{} : {} ", channelKey, description);
        }
        log.info("--------------------------------------");
    }

    /**
     * 初始化消息拦截器
     * <p>
     * 使用ServiceLoader加载所有实现了MessageInterceptor接口的类，
     * 并按照Order注解的值进行排序。
     * </p>
     */
    public static void initMessageInterceptor(){
        // 使用ServiceLoader加载所有拦截器
        ServiceLoader<MessageInterceptor> interceptors = ServiceLoader.load(MessageInterceptor.class);
        for (MessageInterceptor interceptor : interceptors) {
            interceptorList.add(interceptor);
        }

        // 根据拦截器的顺序属性进行排序
        interceptorList.sort(Comparator.comparingInt(MessageInterceptor::getOrder));
    }

    /**
     * 获取指定的消息通道
     * 
     * @param channelKey 通道键，格式为 "type:name"
     * @param config 配置属性（当前版本未使用）
     * @return 消息通道实例
     * @throws ChannelLoadException 如果通道不存在
     */
    public static MessageChannel getChannel(String channelKey, Properties config) {
        if(channels.containsKey(channelKey)) {
            return channels.get(channelKey);
        }
        throw new ChannelLoadException("消息通道不存在："+ channelKey);
    }

    /**
     * 获取拦截器处理器
     * <p>
     * 为指定的消息通道创建拦截器处理器，并添加所有已注册的拦截器。
     * </p>
     * 
     * @param messageChannel 消息通道
     * @return 配置了所有拦截器的处理器
     */
    public static InterceptorHandler getInterceptorHandler(MessageChannel messageChannel){
        InterceptorHandler handler = new InterceptorHandler(messageChannel);
        // 按顺序添加拦截器
        for (MessageInterceptor interceptor : interceptorList) {
            handler.addInterceptor(interceptor);
        }
        return handler;
    }

    public static MessageType getMessageType(String channelKey) {
        if(channels.containsKey(channelKey)) {
            MessageChannel messageChannel = channels.get(channelKey);
            ChannelMetadata metadata = messageChannel.getMetadata();
            return metadata.getType();
        }
        throw new ChannelLoadException("消息通道不存在："+ channelKey);
    }


    public static ChannelMetadata getMetadata(String channelKey) {
        if(channels.containsKey(channelKey)) {
            MessageChannel messageChannel = channels.get(channelKey);
            return messageChannel.getMetadata();
        }
        throw new ChannelLoadException("消息通道不存在："+ channelKey);
    }

    public static Collection<MessageChannel> getAllChannels() {
        return channels.values();
    }

    public static Set<String> getAllChannelName() {
        return channels.keySet();
    }

}
