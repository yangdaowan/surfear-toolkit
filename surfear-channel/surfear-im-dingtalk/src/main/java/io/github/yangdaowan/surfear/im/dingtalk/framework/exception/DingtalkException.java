package io.github.yangdaowan.surfear.im.dingtalk.framework.exception;

import io.github.yangdaowan.surfear.core.exception.MessageChannelException;

/**
 * @author ycf
 **/
public class DingtalkException extends MessageChannelException {

    public DingtalkException(String message) {
        super(message);
    }

    public DingtalkException(String message, Throwable cause) {
        super(message, cause);
    }
}
