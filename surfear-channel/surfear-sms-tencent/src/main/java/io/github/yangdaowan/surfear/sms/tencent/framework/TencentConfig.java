package io.github.yangdaowan.surfear.sms.tencent.framework;

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
@ConfigPrefix("tencent")
public class TencentConfig extends AbsMD5HexConfig {

    @Unique
    private String secretId;
    private String secretKey;

}
