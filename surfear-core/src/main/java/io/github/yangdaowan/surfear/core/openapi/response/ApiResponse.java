package io.github.yangdaowan.surfear.core.openapi.response;

import com.alibaba.fastjson2.JSONObject;

public interface ApiResponse {

    JSONObject parseResponse(String body);

}
