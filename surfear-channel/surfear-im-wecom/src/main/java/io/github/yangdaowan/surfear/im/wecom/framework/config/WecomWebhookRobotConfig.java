package io.github.yangdaowan.surfear.im.wecom.framework.config;

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
@ConfigPrefix("wecom.webhook-robot")
public class WecomWebhookRobotConfig extends AbsMD5HexConfig {

    /**
     * 是否必填: 是<br/>
     * 说明: 自定义机器人调用接口的凭证。
     */
    @Unique
    private String accessToken;

}