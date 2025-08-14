package io.github.yangdaowan.surfear.im.dingtalk.framework.config;

import io.github.yangdaowan.surfear.core.spi.client.AbsMD5HexConfig;
import io.github.yangdaowan.surfear.core.spi.client.Unique;
import io.github.yangdaowan.surfear.core.spi.client.ConfigPrefix;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigPrefix("dingtalk")
public class DingtalkConfig extends AbsMD5HexConfig {

    @Unique
    private String clientId;
    private String clientSecret;

}
