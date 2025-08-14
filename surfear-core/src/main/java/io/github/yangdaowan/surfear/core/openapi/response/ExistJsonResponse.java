package io.github.yangdaowan.surfear.core.openapi.response;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.exception.MessageChannelException;

/**
 * @author ycf
 **/
public class ExistJsonResponse implements ApiResponse {

    private final String codeKey;
    private final String msgKey;
    private final String containsKey;

    public ExistJsonResponse(String codeKey, String msgKey, String containsKey) {
        this.codeKey = codeKey;
        this.msgKey = msgKey;
        this.containsKey = containsKey;
    }

    @Override
    public JSONObject parseResponse(String body) {
        JSONObject jsonResponse = JSONObject.parseObject(body);
        String code = jsonResponse.getString(codeKey);
        if (jsonResponse.containsKey(containsKey)) {
            return jsonResponse;
        } else {
            // 获取错误消息
            String msg = jsonResponse.getString(msgKey);
            throw new MessageChannelException(code + " " + msg);
        }
    }
}
