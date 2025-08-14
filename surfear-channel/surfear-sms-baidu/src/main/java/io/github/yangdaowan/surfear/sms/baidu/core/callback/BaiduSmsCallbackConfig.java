package io.github.yangdaowan.surfear.sms.baidu.core.callback;

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
public class BaiduSmsCallbackConfig extends SmsCallbackConfig {

    private final String serviceName = "百度云短信";
    private Integer port = 21000;
    private String path = "/baidu/sms/callback";

}
