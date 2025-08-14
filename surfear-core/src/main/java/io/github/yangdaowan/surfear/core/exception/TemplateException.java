package io.github.yangdaowan.surfear.core.exception;

/**
 * 模板异常
 * @author ycf
 **/
public class TemplateException extends MessageException {

    public TemplateException(String message) {
        super(message);
    }

    public TemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
