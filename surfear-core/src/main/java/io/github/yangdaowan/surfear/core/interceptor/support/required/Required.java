package io.github.yangdaowan.surfear.core.interceptor.support.required;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 必需字段注解
 * <p>
 * 用于标记模型类中的必需字段，在消息发送前会进行校验。
 * </p>
 * 
 * @author ycf
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {

    /**
     * 提示信息
     */
    String message() default "字段 {} 是必需的";

}
