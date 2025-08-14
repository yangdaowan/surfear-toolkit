package io.github.yangdaowan.surfear.sms.aliyun.framework;

import io.github.yangdaowan.surfear.core.exception.MessageChannelException;

/**
 * @author ycf
 **/
public class AliyunException extends MessageChannelException {

    public AliyunException(String message) {
        super(message);
    }

    public AliyunException(String message, Throwable cause) {
        super(message, cause);
    }
}
