package io.github.yangdaowan.surfear.core.interceptor.support.required;

import io.github.yangdaowan.surfear.core.exception.MessageException;

/**
 * @author ycf
 **/
public class CheckUtil {

    /**
     * 检查必填参数是否为空
     *
     * @param value     参数值
     * @param paramName 参数名称
     * @return 参数值
     */
    public static String requireNonNull(String value, String paramName) {
        if (value == null || value.isEmpty()) {
            throw new MessageException("必填参数缺失："+ paramName + " 不得为 null 或为空");
        }
        return value;
    }

}
