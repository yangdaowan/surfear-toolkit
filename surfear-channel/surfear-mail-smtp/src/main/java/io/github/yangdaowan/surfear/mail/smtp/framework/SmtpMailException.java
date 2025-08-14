package io.github.yangdaowan.surfear.mail.smtp.framework;

import io.github.yangdaowan.surfear.core.exception.MessageChannelException;

/**
 * @author ycf
 **/
public class SmtpMailException extends MessageChannelException {

    public SmtpMailException(String message) {
        super(message);
    }

    public SmtpMailException(String message, Throwable cause) {
        super(message, cause);
    }
}
