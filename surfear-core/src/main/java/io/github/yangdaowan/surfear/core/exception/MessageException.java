package io.github.yangdaowan.surfear.core.exception;

/**
 * 消息异常
 * @author ycf
 **/
public class MessageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
