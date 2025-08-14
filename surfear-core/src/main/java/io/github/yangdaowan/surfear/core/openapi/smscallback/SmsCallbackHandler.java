package io.github.yangdaowan.surfear.core.openapi.smscallback;

/**
 * 短信回调处理器接口
 * @author ycf
 **/
public interface SmsCallbackHandler<T> {

    /**
     * 处理状态报告回调
     * @param callbackData 回调数据
     */
    void handleStatusReport(T callbackData);

    /**
     * 处理上行短信回调
     * @param callbackData 回调数据
     */
    void handleUplinkMessage(T callbackData);

    /**
     * 处理未知类型回调
     * @param callbackData 回调数据
     */
    default void handleUnknownCallback(T callbackData) {
        // 默认实现，子类可覆盖
    }

}
