package io.github.yangdaowan.surfear.sms.tencent.framework;

import io.github.yangdaowan.surfear.core.exception.MessageChannelException;

/**
 * @author ycf
 **/
public class TencentException extends MessageChannelException {

    public TencentException(String message) {
        super(message);
    }

    public TencentException(String message, Throwable cause) {
        super(message, cause);
    }
}
