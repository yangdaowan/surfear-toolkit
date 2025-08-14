package io.github.yangdaowan.surfear.core.spi.message;


/**
 * 消息类型枚举
 *
 * @author ycf
 **/
public enum MessageType {

    /**
     * 邮件
     */
    MAIL,
    /**
     * 短信
     */
    SMS,
    /**
     * 站内信 sse、websocket
     */
    PUSH,
    /**
     * 苹果、安卓
     */
    APP,
    /**
     * IM工具 微信、钉钉、飞书
     */
    IM

}
