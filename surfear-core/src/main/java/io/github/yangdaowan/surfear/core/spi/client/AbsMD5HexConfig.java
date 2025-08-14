package io.github.yangdaowan.surfear.core.spi.client;

import io.github.yangdaowan.surfear.core.exception.ConfigException;
import io.github.yangdaowan.surfear.core.util.SignUtil;

/**
 * @author ycf
 **/
public abstract class AbsMD5HexConfig extends AbsConfig {

    @Override
    public String getId() {
        String clientKey = uniqueConfigurationItem();
        if (clientKey == null || clientKey.trim().isEmpty()) {
            throw new ConfigException("未指定客户端唯一配置项");
        }
        return SignUtil.md5Hex(clientKey);
    }

}
