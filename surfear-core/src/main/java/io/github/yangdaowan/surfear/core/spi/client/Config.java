package io.github.yangdaowan.surfear.core.spi.client;

public interface Config {

    /**
     * 获取配置项的唯一标识符。
     * 该标识符通常用于区分不同的配置实例。
     *
     * @return 配置项的唯一标识符
     */
    String getId();

}
