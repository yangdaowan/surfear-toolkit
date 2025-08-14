package io.github.yangdaowan.surfear.sms.baidu.framework;

import io.github.yangdaowan.surfear.core.exception.MessageChannelException;

/**
 * @author ycf
 **/
public class BaiduException extends MessageChannelException {

    public BaiduException(String message) {
        super(message);
    }

    public BaiduException(String message, Throwable cause) {
        super(message, cause);
    }
}
