package io.github.yangdaowan.surfear.core.exception;

/**
 * 参数校验异常
 * @author ycf
 **/
public class ModelParamValidateException extends MessageException {


    public ModelParamValidateException(String message) {
        super(message);
    }

    public ModelParamValidateException(String message, Throwable cause) {
        super(message, cause);
    }
}
