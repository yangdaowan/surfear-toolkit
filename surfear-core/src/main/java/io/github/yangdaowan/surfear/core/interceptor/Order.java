package io.github.yangdaowan.surfear.core.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排序顺序注解
 * <p>
 * 用于指定组件的执行顺序，数字越小优先级越高。
 * 如果未指定该注解，默认排序值为99。
 * </p>
 * 
 * @author ycf
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {

    int value();
}
