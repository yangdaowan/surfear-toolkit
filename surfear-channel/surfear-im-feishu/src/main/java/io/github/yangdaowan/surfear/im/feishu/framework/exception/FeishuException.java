package io.github.yangdaowan.surfear.im.feishu.framework.exception;

import io.github.yangdaowan.surfear.core.exception.MessageChannelException;

public class FeishuException extends MessageChannelException {

    public FeishuException(String message) {
        super(message);
    }

    public FeishuException(String message, Throwable cause) {
        super(message, cause);
    }
}
