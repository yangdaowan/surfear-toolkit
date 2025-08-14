package io.github.yangdaowan.surfear.core.exception;

/**
 * 签名异常
 * @author ycf
 **/
public class SignException extends MessageException {

    public SignException(String message) {
        super(message);
    }

    public SignException(String message, Throwable cause) {
        super(message, cause);
    }
}
