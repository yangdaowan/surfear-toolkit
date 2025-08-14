package io.github.yangdaowan.surfear.im.wecom.framework.exception;

import io.github.yangdaowan.surfear.core.exception.MessageChannelException;

public class WecomException extends MessageChannelException {

    public WecomException(String message) {
        super(message);
    }

    public WecomException(String message, Throwable cause) {
        super(message, cause);
    }
}
