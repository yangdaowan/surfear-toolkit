package io.github.yangdaowan.surfear.core.openapi.response;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.exception.ExceptionUtils;
import io.github.yangdaowan.surfear.core.exception.MessageChannelException;

/**
 * @author ycf
 **/
public class JsonResponse implements ApiResponse {

    private final String codeKey;
    private final String msgKey;
    private final String successCode;
    private Class<? extends MessageChannelException> exception;

    public JsonResponse(String codeKey, String msgKey, String successCode) {
        this.codeKey = codeKey;
        this.msgKey = msgKey;
        this.successCode = successCode;
    }

    public JsonResponse throwException(Class<? extends MessageChannelException> exception){
        this.exception = exception;
        return this;
    }

    @Override
    public JSONObject parseResponse(String body) {
        JSONObject jsonResponse = JSONObject.parseObject(body);
        String code = jsonResponse.getString(codeKey);
        if (successCode.equals(code)) {
            return jsonResponse;
        } else {
            // 获取错误消息
            String msg = jsonResponse.getString(msgKey);
            if(exception != null){
                throw ExceptionUtils.throwException(exception, code + " " +msg);
            } else {
                throw new MessageChannelException(code + " " + msg);
            }
        }
    }
}
