package io.github.yangdaowan.surfear.sms.aliyun.core;

import io.github.yangdaowan.surfear.core.util.SignUtil;
import io.github.yangdaowan.surfear.sms.aliyun.core.api.base.AliyunOpenApi;
import io.github.yangdaowan.surfear.sms.aliyun.framework.AliyunConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @see <a href="https://help.aliyun.com/zh/sdk/product-overview/v3-request-structure-and-signature?spm=api-workbench.API%20Document.0.0.5be349a5A9vuMQ#79cbd5a0c1gif">阿里云V3签名规则</a>
 * @see <a href="https://next.api.aliyun.com/document/Dysmsapi/2017-05-25/SendSms">阿里云短信接口</a>
 * @see <a href="https://next.api.aliyun.com/meta/v1/products/Dysmsapi/versions/2017-05-25/apis/SendSms/api?spm=api-workbench.API%20Document.0.0.5be31c6csLRD1q">接口元数据</a>
 * @author ycf
 **/
public class AliyunApiSignV3 {

    private static final String ALGORITHM = "ACS3-HMAC-SHA256";
    private static final Logger log = LoggerFactory.getLogger(AliyunApiSignV3.class);

    /**
     * 生成V3认证字符串
     */
    public static void generateAuthorization(AliyunOpenApi api, AliyunConfig config) {
        String accessKey = config.getAccessKey();
        String secretKey = config.getSecretKey();

        String httpMethod = api.getHttpMethod();
        String path = api.getCanonicalUri();
        Map<String, Object> params = api.getQueryParam();
        Map<String, String> headers = api.getHeaders();

        log.debug("params: \n{}", params);

        // 生成规范化请求字符串
        String canonicalRequest = generateCanonicalRequest(httpMethod, path, params, headers);
        log.debug("生成规范化请求字符串: \n{}", canonicalRequest);

        // 生成待签名字符串
        String hashedCanonicalRequest = SignUtil.sha256Hex(canonicalRequest);
        String stringToSign = ALGORITHM + "\n" + hashedCanonicalRequest;
        log.debug("生成待签名字符串: \n{}", stringToSign);

        // 计算签名
        String signature = SignUtil.hmacSha256Hex(secretKey, stringToSign);
        log.debug("计算签名 signature: \n{}", signature);

        // 生成认证字符串
        String authorization = generateAuthString(accessKey, headers, signature);
        log.debug("生成认证字符串 authorization: \n{}", authorization);
        headers.put("Authorization", authorization);
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
        String canonicalHeaders = generateCanonicalHeaders(headers);
        canonicalRequest.append(canonicalHeaders).append("\n");

        // 5. SignedHeaders
        String signedHeaders = headers.keySet().stream()
                .map(String::toLowerCase)
                .sorted()
                .collect(Collectors.joining(";"));
        canonicalRequest.append(signedHeaders).append("\n");

        // 6. HashedRequestPayload
        String hashedRequestPayload = headers.get("x-acs-content-sha256");
        canonicalRequest.append(hashedRequestPayload);

        return canonicalRequest.toString();
    }

    /**
     * 生成规范化头信息
     */
    public static String generateCanonicalHeaders(Map<String, String> headers) {
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
            canonicalHeaders.append(entry.getKey())
                    .append(":")
                    .append(entry.getValue())
                    .append("\n");
        }

        return canonicalHeaders.toString();
    }

    /**
     * 生成认证字符串
     */
    private static String generateAuthString(String accessKey,
                                           Map<String, String> headers,
                                           String signature) {
        StringBuilder authString = new StringBuilder();
        authString.append(ALGORITHM)
                .append(" ")
                .append("Credential=")
                .append(accessKey)
                .append(",SignedHeaders=");

        // 添加签名头
        String signedHeaders = headers.keySet().stream()
                .map(String::toLowerCase)
                .sorted()
                .collect(Collectors.joining(";"));
        authString.append(signedHeaders)
                .append(",Signature=")
                .append(signature);

        return authString.toString();
    }

    /**
     * 递归处理对象，将复杂对象（如Map和List）展开为平面的键值对
     */
    private static void processObject(Map<String, Object> flatMap, String prefix, Object value) {
        if (value == null) {
            return;
        }

        // 移除前缀中多余的"."
        prefix = prefix != null && prefix.startsWith(".") ? prefix.substring(1) : prefix;

        // 处理List类型
        if (value instanceof List<?>) {
            List<?> list = (List<?>) value;
            for (int i = 0; i < list.size(); i++) {
                processObject(flatMap, prefix + "." + (i + 1), list.get(i));
            }
        }
        // 处理Map类型
        else if (value instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) value;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                processObject(flatMap, prefix + "." + entry.getKey(), entry.getValue());
            }
        }
        // 处理byte[]类型
        else if (value instanceof byte[]) {
            flatMap.put(prefix, new String((byte[]) value, StandardCharsets.UTF_8));
        }
        // 处理其他类型
        else {
            if (!prefix.isEmpty()) {
                flatMap.put(prefix, value);
            }
        }
    }
}