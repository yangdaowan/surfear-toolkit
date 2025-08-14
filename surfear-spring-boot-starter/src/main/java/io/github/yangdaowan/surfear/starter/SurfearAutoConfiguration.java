package io.github.yangdaowan.surfear.starter;

import io.github.yangdaowan.surfear.core.spi.MessageChannelRegistry;
import io.github.yangdaowan.surfear.core.util.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Surfear自动配置类
 * <p>
 * 负责在Spring Boot应用启动时初始化Surfear消息通道。
 * 确保SPI加载的消息通道在Spring容器中正确可用。
 * </p>
 * 
 * <h3>配置说明：</h3>
 * <ul>
 *   <li>保持原有的surfear.yaml配置文件方式</li>
 *   <li>仅负责初始化SPI通道，不提供额外配置绑定</li>
 *   <li>可选服务（SSE、短信回调）需要用户手动配置Bean</li>
 * </ul>
 * 
 * @author ycf
 * @see MessageSender
 * @see MessageChannelRegistry
 */
@Configuration
@ConditionalOnClass(MessageSender.class)
@Slf4j
public class SurfearAutoConfiguration {

    /**
     * Surfear通道初始化器
     * <p>
     * 确保MessageChannelRegistry的SPI初始化在Spring容器启动时完成。
     * 这是一个标记Bean，主要作用是触发MessageChannelRegistry的静态初始化，
     * 并在日志中显示可用的消息通道。
     * </p>
     * 
     * @return 初始化器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public SurfearChannelInitializer surfearChannelInitializer() {
        log.info("=== Surfear消息通知工具包初始化 ===");
        log.info("配置文件: 请确保 surfear.yaml 或 surfear.properties 在classpath中");
        log.info("使用方式: MessageSender.send(channelKey, model)");
        log.info("=== Surfear初始化完成 ===");
        return new SurfearChannelInitializer();
    }
    
    /**
     * 通道初始化器类
     * <p>
     * 空实现类，仅用于确保SPI加载在Spring容器中正确执行。
     * 该Bean的创建会触发MessageChannelRegistry的静态初始化块执行。
     * </p>
     * 
     * @author ycf
     */
    public static class SurfearChannelInitializer {
        /**
         * 获取所有可用的通道名称
         * 
         * @return 通道名称集合
         */
        public java.util.Set<String> getAvailableChannels() {
            return MessageChannelRegistry.getAllChannelName();
        }
        
        /**
         * 检查指定通道是否可用
         * 
         * @param channelKey 通道键，格式为 "type:name"
         * @return 如果通道存在返回true，否则返回false
         */
        public boolean isChannelAvailable(String channelKey) {
            return MessageChannelRegistry.getAllChannelName().contains(channelKey);
        }
    }
}
