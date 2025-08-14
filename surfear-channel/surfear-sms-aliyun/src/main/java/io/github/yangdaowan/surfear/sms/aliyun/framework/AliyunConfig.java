package io.github.yangdaowan.surfear.sms.aliyun.framework;

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
@ConfigPrefix("aliyun")
public class AliyunConfig extends AbsMD5HexConfig {

    // accessKey
    @Unique
    private String accessKey;
    // secretKey
    private String secretKey;

}
