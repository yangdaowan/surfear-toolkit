package io.github.yangdaowan.surfear.core.exception;

/**
 * 回调服务异常
 * <p>
 * 用于表示回调服务相关的异常情况。
 * </p>
 * 
 * @author ycf
 */
public class CallbackException extends RuntimeException {

    public CallbackException(String message) {
        super(message);
    }

    public CallbackException(String message, Throwable cause) {
        super(message, cause);
    }

    public CallbackException(Throwable cause) {
        super(cause);
    }
}
