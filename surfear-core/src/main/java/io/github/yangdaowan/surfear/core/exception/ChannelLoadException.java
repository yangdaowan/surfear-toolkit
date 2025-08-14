package io.github.yangdaowan.surfear.core.exception;

/**
 * 插件加载异常
 * @author ycf
 **/
public class ChannelLoadException extends MessageException {

    public ChannelLoadException(String message) {
        super(message);
    }

    public ChannelLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
