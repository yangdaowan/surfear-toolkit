package io.github.yangdaowan.surfear.sms.baidu.core;

import io.github.yangdaowan.surfear.core.util.SignUtil;
import io.github.yangdaowan.surfear.sms.baidu.core.api.base.BaiduOpenApi;
import io.github.yangdaowan.surfear.sms.baidu.framework.BaiduConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 百度云API V1签名工具类
 * @author ycf
 * @see <a href="https://cloud.baidu.com/doc/Reference/s/njwvz1yfu">百度云V1签名规则</a>
 * @see <a href="https://cloud.baidu.com/signature/index.html">签名计算工具</a>
 */
public class BaiduApiSignV1 {


    private static final String BCE_AUTH_VERSION = "bce-auth-v1";
    private static final int DEFAULT_EXPIRE_SECONDS = 1800;
    private static final Logger log = LoggerFactory.getLogger(BaiduApiSignV1.class);

    /**
     * 生成V1认证字符串（指定时间戳）
     */
    public static void generateAuthorization(BaiduOpenApi api, BaiduConfig config) {
        String accessKey = config.getAccessKey();
        String secretKey = config.getSecretKey();

        String httpMethod = api.getHttpMethod();
        String path = api.getCanonicalUri();
        Map<String, Object> params = api.getQueryParam();
        Map<String, String> headers = api.getHeaders();
        String timestamp = api.getHeaders().get("x-bce-date");

        log.debug("timestamp: \n{}", timestamp);
        log.debug("params: \n{}", params);

        // 生成规范化请求字符串
        String canonicalRequest = SignUtil.removeTrailingNewline(generateCanonicalRequest(httpMethod, path, params, headers));
        log.debug("生成规范化请求字符串: \n{}", canonicalRequest);

        // 前缀字符串
        String authStringPrefix = BCE_AUTH_VERSION + "/" + accessKey + "/" + timestamp + "/" + DEFAULT_EXPIRE_SECONDS;
        log.debug("前缀字符串: \n{}", authStringPrefix);

        // 生成SigningKey
        String signingKey = SignUtil.hmacSha256Hex(secretKey, authStringPrefix);
        log.debug("生成 signingKey: \n{}", signingKey);

        // 计算签名
        String signature = SignUtil.hmacSha256Hex(signingKey, canonicalRequest);
        log.debug("计算签名 signature: \n{}", signature);

        // 生成认证字符串
        String authorization = generateAuthString(accessKey, timestamp, headers, signature);
        headers.put("Authorization", authorization);
        log.debug("生成认证字符串 authorization: \n{}", authorization);
    }

    /**
     * 生成规范化请求字符串
     */
    private static String generateCanonicalRequest(String httpMethod, String path,
                                                 Map<String, Object> params,
                                                 Map<String, String> headers) {
        StringBuilder canonicalRequest = new StringBuilder();

        // 1. HTTP Method
        canonicalRequest.append(httpMethod.toUpperCase()).append("\n");

        // 2. CanonicalURI
        canonicalRequest.append(path).append("\n");

        // 3. CanonicalQueryString
        String canonicalQueryString = SignUtil.generateCanonicalQueryString(params);
        canonicalRequest.append(canonicalQueryString).append("\n");

        // 4. CanonicalHeaders
        String canonicalHeaders = generateCanonicalHeaders(headers, true);
        canonicalRequest.append(canonicalHeaders);

        return canonicalRequest.toString();
    }

    /**
     * 生成规范化头信息
     */
    public static String generateCanonicalHeaders(Map<String, String> headers, boolean isUrlEncode) {
        if (headers == null || headers.isEmpty()) {
            return "";
        }

        // 1. 转换成小写，并排序
        Map<String, String> sortedHeaders = headers.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toLowerCase(),
                        Map.Entry::getValue,
                        (v1, v2) -> v1,
                        TreeMap::new
                ));

        // 2. 生成规范化头信息
        StringBuilder canonicalHeaders = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedHeaders.entrySet()) {
            String key = entry.getKey();
            if(key.equalsIgnoreCase("host") || key.toLowerCase().startsWith("x-bce")){
                canonicalHeaders.append(isUrlEncode?SignUtil.urlEncode(key):key)
                        .append(":")
                        .append(isUrlEncode?SignUtil.urlEncode(entry.getValue()):entry.getValue())
                        .append("\n");
            }
        }

        return canonicalHeaders.toString();
    }

    /**
     * 生成认证字符串
     */
    private static String generateAuthString(String accessKey, String timestamp,
                                           Map<String, String> headers, String signature) {
        StringBuilder authString = new StringBuilder();
        authString.append(BCE_AUTH_VERSION)
                .append("/")
                .append(accessKey)
                .append("/")
                .append(timestamp)
                .append("/")
                .append(DEFAULT_EXPIRE_SECONDS);

        // 添加签名头
        if (headers != null && !headers.isEmpty()) {
            authString.append("/");
            String signedHeaders = headers.keySet().stream()
                    .filter(key -> key.equalsIgnoreCase("host") || key.toLowerCase().startsWith("x-bce"))
                    .map(String::toLowerCase)
                    .sorted()
                    .collect(Collectors.joining(";"));
            authString.append(signedHeaders);
        }

        // 添加签名
        authString.append("/").append(signature);

        return authString.toString();
    }


}