package io.github.yangdaowan.surfear.core.openapi.smscallback;

import lombok.Data;

/**
 * @author ycf
 **/
@Data
public class SmsCallbackConfig {

    private String serviceName;     // 服务名称
    private Integer port;               // 服务监听端口
    private String path;             // 回调路径

}

