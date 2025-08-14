package io.github.yangdaowan.surfear.core.exception;

/**
 * 发送失败异常
 * @author ycf
 **/
public class SendFailedException extends MessageException {


    public SendFailedException(String message) {
        super(message);
    }

    public SendFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
