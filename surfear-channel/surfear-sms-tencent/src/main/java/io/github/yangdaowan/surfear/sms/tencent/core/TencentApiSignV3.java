package io.github.yangdaowan.surfear.sms.tencent.core;

import io.github.yangdaowan.surfear.core.util.SignUtil;
import io.github.yangdaowan.surfear.sms.tencent.core.api.base.TencentOpenApi;
import io.github.yangdaowan.surfear.sms.tencent.framework.TencentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author ycf
 * @see <a href="https://cloud.tencent.com/document/api/382/55981">腾讯云短信接口文档</a>
 * @see <a href="https://cloud.tencent.com/document/api/382/52072">腾讯云V3签名文档</a>
 **/
public class TencentApiSignV3 {

    private static final String ALGORITHM = "TC3-HMAC-SHA256";
    private static final Logger log = LoggerFactory.getLogger(TencentApiSignV3.class);

    /**
     * 生成V3认证字符串
     */
    public static void generateAuthorization(TencentOpenApi api, TencentConfig config) {
        String accessKey = config.getSecretId();
        String secretKey = config.getSecretKey();

        Map<String, String> headers = api.getHeaders();

        String timestamp = headers.get("X-TC-Timestamp");
        String date = headers.get("X-TC-Date");

        String service = headers.get("X-TC-Service");
//        String action = headers.get("X-TC-Action");
//        String version = headers.get("X-TC-Version");
//        String region = headers.get("X-TC-Region");

        Map<String, String> signedHeaderMap = headers.entrySet().stream()
                .filter(s -> "host".equalsIgnoreCase(s.getKey()) || "content-type".equalsIgnoreCase(s.getKey()))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toLowerCase(),
                        Map.Entry::getValue,
                        (v1, v2) -> v1,
                        TreeMap::new
                ));

        String signedHeaders = signedHeaderMap.keySet().stream()
                .map(String::toLowerCase)
                .sorted()
                .collect(Collectors.joining(";"));

        // 1. 生成规范化请求字符串
        String canonicalRequest = generateCanonicalRequest(api, signedHeaderMap, signedHeaders);
        log.debug("生成规范化请求字符串 canonicalRequest : \n{}", canonicalRequest);

        // 2. 拼接待签名字符串
        String credentialScope = date + "/" + service + "/" + "tc3_request";
        String stringToSign = generateStringToSign(canonicalRequest, timestamp, credentialScope);
        log.debug("拼接待签名字符串 stringToSign : \n{}", stringToSign);

        // 3. 计算签名
        String signature = generateSignature(secretKey, stringToSign, date, service);
        log.debug("计算签名 signature : \n{}", signature);

        // 4. 生成认证字符串
        String authorization = generateAuthString(accessKey, credentialScope, signedHeaders, signature);
        log.debug("生成认证字符串 authorization : \n{}", authorization);
        headers.put("Authorization", authorization);
    }

    /**
     * 拼接待签名字符串
     */
    private static String generateStringToSign(String canonicalRequest, String timestamp, String credentialScope) {
        String hashedCanonicalRequest = SignUtil.sha256Hex(canonicalRequest);
        return ALGORITHM + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;
    }

    /**
     * 生成规范化请求字符串
     */
    private static String generateCanonicalRequest(TencentOpenApi api,  Map<String, String> signedHeaderMap, String signedHeaders) {
        String httpMethod = api.getHttpMethod().toUpperCase();
        String canonicalUri = api.getCanonicalUri();
        Map<String, Object> queryParam = api.getQueryParam();
        String body = api.getBody();

        StringBuilder canonicalRequest = new StringBuilder();

        // 1. HTTP Method
        canonicalRequest.append(httpMethod).append("\n");

        // 2. CanonicalURI
        canonicalRequest.append(canonicalUri).append("\n");

        // 3. CanonicalQueryString
        String canonicalQueryString = SignUtil.generateCanonicalQueryString(queryParam);
        canonicalRequest.append(canonicalQueryString).append("\n");

        // 4. CanonicalHeaders
        String canonicalHeaders = generateCanonicalHeaders(signedHeaderMap);
        canonicalRequest.append(canonicalHeaders).append("\n");

        // 5. SignedHeaders
        canonicalRequest.append(signedHeaders).append("\n");

        // 6. HashedRequestPayload
        String hashedRequestPayload = body != null ? SignUtil.sha256Hex(body) : SignUtil.sha256Hex("");
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
     * 生成签名
     */
    private static String generateSignature(String secretKey, String stringToSign, String date, String service) {
        byte[] secretDate = SignUtil.hmac256(("TC3" + secretKey).getBytes(StandardCharsets.UTF_8), date);
        byte[] secretService = SignUtil.hmac256(secretDate, service);
        byte[] secretSigning = SignUtil.hmac256(secretService, "tc3_request");
        return DatatypeConverter.printHexBinary(SignUtil.hmac256(secretSigning, stringToSign)).toLowerCase();
    }

    /**
     * 生成认证字符串
     */
    private static String generateAuthString(String accessKey, String credentialScope,
                                             String signedHeaders, String signature) {

        return ALGORITHM +
                " " +
                "Credential=" +
                accessKey +
                "/" +
                credentialScope +
                ", " +
                "SignedHeaders=" +
                signedHeaders +
                ", " +
                "Signature=" +
                signature;
    }
}
