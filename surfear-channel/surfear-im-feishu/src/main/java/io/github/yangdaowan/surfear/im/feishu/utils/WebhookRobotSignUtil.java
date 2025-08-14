package io.github.yangdaowan.surfear.im.feishu.utils;

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
     */
    public static String sign(int timestamp, String secret) {
        try {
            //把timestamp+"\n"+密钥当做签名字符串
            String stringToSign = timestamp + "\n" + secret;
            //使用HmacSHA256算法计算签名
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(new byte[]{});
            return new String(Base64.getEncoder().encode(signData));
        } catch (Exception e) {
            throw new SignException("URL 安全签名失败：", e);
        }
    }
}
