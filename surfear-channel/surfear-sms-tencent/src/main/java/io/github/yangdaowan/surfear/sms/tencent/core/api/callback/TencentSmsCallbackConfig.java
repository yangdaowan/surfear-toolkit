package io.github.yangdaowan.surfear.sms.tencent.core.api.callback;

import io.github.yangdaowan.surfear.core.openapi.smscallback.SmsCallbackConfig;
import lombok.*;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TencentSmsCallbackConfig extends SmsCallbackConfig {

    private final String serviceName = "腾讯云短信";
    private Integer port = 21002;
    private String path = "/tencent/sms/callback";

}
