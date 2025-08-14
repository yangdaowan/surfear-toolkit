package io.github.yangdaowan.surfear.sms.aliyun.core.api.base;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.openapi.response.JsonResponse;
import io.github.yangdaowan.surfear.core.util.SignUtil;
import io.github.yangdaowan.surfear.sms.aliyun.framework.AliyunException;

import java.util.UUID;

/**
 * @author ycf
 **/
public abstract class AliyunOpenApi extends AbsOpenApi {

    public void initHeader(String action, String version) {
        this.headers.put("host", host);
        this.headers.put("x-acs-action", action);
        this.headers.put("x-acs-version", version);
        this.headers.put("x-acs-date", SignUtil.utcTimestamp());
        this.headers.put("x-acs-signature-nonce", UUID.randomUUID().toString());

        // 计算请求体的哈希值
        String requestPayload = ""; // 请求体，当请求正文为空时，比如GET请求，RequestPayload固定为空字符串
        String hashedRequestPayload = this.body != null ? SignUtil.sha256Hex(this.body) : SignUtil.sha256Hex(requestPayload);
        this.headers.put("x-acs-content-sha256", hashedRequestPayload);
    }

    public JSONObject parseResponse(String body) {
        return new JsonResponse("Code", "Message", "OK")
                .throwException(AliyunException.class)
                .parseResponse(body);
    }
}

