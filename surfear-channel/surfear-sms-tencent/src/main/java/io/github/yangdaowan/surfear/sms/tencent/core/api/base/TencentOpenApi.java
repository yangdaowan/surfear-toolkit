package io.github.yangdaowan.surfear.sms.tencent.core.api.base;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.exception.MessageException;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.util.SignUtil;
import io.github.yangdaowan.surfear.sms.tencent.framework.TencentException;

/**
 * @author ycf
 **/
public abstract class TencentOpenApi extends AbsOpenApi {


    public void initHeader(String service, String action, String version, String region) {
        this.headers.put("host", host);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String date = SignUtil.utcDate(timestamp);

        this.headers.put("X-TC-Timestamp", timestamp);
        this.headers.put("X-TC-Date", date);

        this.headers.put("X-TC-Service", service);
        this.headers.put("X-TC-Action", action);
        this.headers.put("X-TC-Version", version);
        this.headers.put("X-TC-Region", region);
        this.headers.put("X-TC-Language", "zh-CN");
    }

    @Override
    public JSONObject parseResponse(String body) {
        JSONObject jsonResponse = JSONObject.parseObject(body);
        JSONObject response = jsonResponse.getJSONObject("Response");
        if(response.containsKey("Error")){
            JSONObject error = response.getJSONObject("Error");
            // 获取错误消息
            String code = error.getString("Code");
            String msg = error.getString("Message");
            throw new TencentException(code + " " + msg);
        }
        return jsonResponse;
    }
}
