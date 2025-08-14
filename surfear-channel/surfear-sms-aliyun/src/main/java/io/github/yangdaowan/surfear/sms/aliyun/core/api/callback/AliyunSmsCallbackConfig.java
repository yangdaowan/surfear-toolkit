package io.github.yangdaowan.surfear.sms.aliyun.core.api.callback;

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
public class AliyunSmsCallbackConfig extends SmsCallbackConfig {

    private final String serviceName = "阿里云短信";
    private Integer port = 21001;
    private String path = "/aliyun/sms/callback";

}
