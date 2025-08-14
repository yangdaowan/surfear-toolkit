package io.github.yangdaowan.surfear.core.exception;

/**
 * 消息通道异常
 * @author ycf
 **/
public class MessageChannelException extends MessageException {

    public MessageChannelException(String message) {
        super(message);
    }

    public MessageChannelException(String message, Throwable cause) {
        super(message, cause);
    }
}
