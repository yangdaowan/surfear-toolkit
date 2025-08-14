package io.github.yangdaowan.surfear.im.feishu.framework.config;

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
@ConfigPrefix("feishu.webhook-robot")
public class FeishuWebhookRobotConfig extends FeishuConfig {

    /**
     * 自定义机器人调用接口的凭证。
     */
    @Unique
    private String accessToken;
    /**
     * 签名秘钥，添加自定义机器人时所生成的密匙(可选)，用于请求的签名认证。填写自动签名
     */
    private String secretKey;

}