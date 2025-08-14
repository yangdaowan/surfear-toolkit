package io.github.yangdaowan.surfear.core.exception;

/**
 * 配置异常
 * @author ycf
 **/
public class ConfigException extends MessageException {

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
