package io.github.yangdaowan.surfear.im.dingtalk.utils;

import io.github.yangdaowan.surfear.core.exception.SignException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 自定义机器人安全设置 签名工具
 * @author ycf
 **/
public class WebhookRobotSignUtil {

    /**
     * 自定义机器人安全设置 签名工具
     **/
    public static String sign(Long timestamp, String secret) {
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(signData), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SignException("URL 安全签名失败：", e);
        }
    }
}
