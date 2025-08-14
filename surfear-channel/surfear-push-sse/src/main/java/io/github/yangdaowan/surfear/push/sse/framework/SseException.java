package io.github.yangdaowan.surfear.push.sse.framework;

import io.github.yangdaowan.surfear.core.exception.MessageChannelException;

/**
 * @author ycf
 **/
public class SseException extends MessageChannelException {

    public SseException(String message) {
        super(message);
    }

    public SseException(String message, Throwable cause) {
        super(message, cause);
    }
}
