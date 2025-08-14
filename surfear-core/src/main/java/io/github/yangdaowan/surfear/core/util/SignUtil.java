package io.github.yangdaowan.surfear.core.util;


import io.github.yangdaowan.surfear.core.exception.SignException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SignUtil {

    /**
     * 将字节数组编码为十六进制字符串
     */
    public static String bytesToHex(byte[] data) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : data) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 使用SHA-256算法计算字符串的哈希值并以十六进制字符串形式返回。
     *
     * @param text 需要进行SHA-256哈希计算的字符串。
     * @return 计算结果为小写十六进制字符串。
     * @throws Exception 如果在获取SHA-256消息摘要实例时发生错误。
     */
    public static String sha256Hex(String text){
        try {
            byte[] input = text.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(input);
            return DatatypeConverter.printHexBinary(d).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new SignException("SHA-256算法计算字符串的哈希值失败", e);
        }
    }

    public static byte[] hmac256(byte[] key, String msg) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
            mac.init(secretKeySpec);
            return mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SignException("HMAC-SHA256算法计算失败", e);
        }
    }

    /**
     * 计算 HMAC-SHA256 的摘要，并返回小写形式的十六进制字符串
     *
     * @param key     密钥
     * @param message 消息
     * @return HMAC-SHA256 摘要的十六进制字符串
     */
    public static String hmacSha256Hex(String key, String message) {
        byte[] hmacBytes = hmac256(key.getBytes(StandardCharsets.UTF_8), message);
        return bytesToHex(hmacBytes).toLowerCase();
    }

    /**
     * 按照ISO 8601标准表示的UTC时间，格式为yyyy-MM-ddTHH:mm:ssZ
     */
    public static String utcTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    /**
     * 检查给定的UTC时间戳是否在当前时间的指定时间范围内
     * @param utcTimestamp UTC时间戳，格式为yyyy-MM-dd'T'HH:mm:ss'Z'
     * @param time 时间范围值
     * @param timeUnit 时间单位（TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS等）
     * @return true 如果在当前时间的指定时间范围内，false 否则
     */
    public static boolean isWithinTimeRange(String utcTimestamp, long time, TimeUnit timeUnit) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date timestampDate = sdf.parse(utcTimestamp);
            Date currentDate = new Date();

            long diffInMillis = currentDate.getTime() - timestampDate.getTime();
            long timeRangeInMillis = timeUnit.toMillis(time);

            return diffInMillis >= 0 && diffInMillis <= timeRangeInMillis;
        } catch (ParseException e) {
            // 如果时间戳格式不正确，返回false
            return false;
        }
    }

    /**
     * 按照ISO 8601标准表示的UTC日期，格式为yyyy-MM-dd
     */
    public static String utcDate(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(Long.parseLong(timestamp + "000")));
    }

    /**
     * 对指定的字符串进行URL编码。
     * 使用UTF-8编码字符集对字符串进行编码，并对特定的字符进行替换，以符合URL编码规范。
     *
     * @param value 需要进行URL编码的字符串。
     * @return 编码后的字符串。其中，加号"+"被替换为"%20"，星号"*"被替换为"%2A"，波浪号"%7E"被替换为"~"。
     */
    public static String urlEncode(String value) {
        if (value == null) {
            throw new SignException("编码的键值对不能为null");
        }
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name())
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new SignException("UTF-8编码不被支持", e);
        }
    }

    /**
     * 生成规范化查询字符串
     */
    public static String generateCanonicalQueryString(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        List<String> paramList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            if(entry.getValue() != null) {
                String value = entry.getValue().toString();
                paramList.add(SignUtil.urlEncode(key) + "=" + SignUtil.urlEncode(value));
            } else {
                paramList.add(SignUtil.urlEncode(key) + "=");
            }
        }

        Collections.sort(paramList);
        return String.join("&", paramList);
    }

    public static String removeTrailingNewline(String str) {
        if (str != null && str.endsWith("\n")) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 计算字符串的 MD5 哈希值并以十六进制字符串形式返回
     *
     * @param data 需要进行MD5哈希计算的字符串
     * @return 32位小写MD5哈希字符串
     * @throws SignException 如果MD5算法不可用
     */
    public static String md5Hex(String data) {
        try {
            byte[] input = data.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input);
            return bytesToHex(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new SignException("md5Hex算法计算字符串的哈希值失败", e);
        }
    }
}
