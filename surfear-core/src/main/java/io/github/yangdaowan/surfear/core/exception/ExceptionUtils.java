package io.github.yangdaowan.surfear.core.exception;

import java.lang.reflect.Constructor;

public class ExceptionUtils {

    /**
     * 动态创建并抛出 ProviderException 或其子类
     * @param exceptionClass 异常类（必须是 ProviderException 或其子类）
     * @param message        错误信息
     */
    public static MessageException throwException (
            Class<? extends MessageChannelException> exceptionClass,
            String message
    ) {
        try {
            Constructor<? extends MessageChannelException> constructor =
                    exceptionClass.getConstructor(String.class);
            return constructor.newInstance(message);
        } catch (ReflectiveOperationException e) {
            throw new MessageException("反射异常，无法创建动态的异常类型", e);
        }
    }

}
