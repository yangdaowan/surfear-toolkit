package io.github.yangdaowan.surfear.sms.baidu.framework;

import io.github.yangdaowan.surfear.core.spi.client.AbsMD5HexConfig;
import io.github.yangdaowan.surfear.core.spi.client.Unique;
import io.github.yangdaowan.surfear.core.spi.client.ConfigPrefix;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigPrefix("baidu")
public class BaiduConfig extends AbsMD5HexConfig {

    @Unique
    private String accessKey;
    private String secretKey;

}
