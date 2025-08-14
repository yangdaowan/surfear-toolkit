package io.github.yangdaowan.surfear.core.openapi.client;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.exception.SendFailedException;
import io.github.yangdaowan.surfear.core.openapi.api.OpenApi;
import io.github.yangdaowan.surfear.core.openapi.bean.FileUpload;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author ycf
 **/
@Slf4j
public class OkhttpClient implements ApiClient<Request, String> {

    public Request build(OpenApi openApi) {
        Request.Builder requestBuilder = new Request.Builder().url(openApi.buildUrl());
        for (Map.Entry<String, String> entry : openApi.getHeaders().entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        log.debug("RequestHeader: {}", JSONObject.from(openApi.getHeaders()).toJSONString());

        Request req;
        switch (openApi.getHttpMethod()) {
            case "GET":
                req = requestBuilder.get().build();
                break;
            case "POST":
                RequestBody requestBody;
                // 处理文件上传
                if (openApi.getFileUpload() != null) {
                    requestBody = buildMultipartBody(openApi);
                } else if (openApi.getBody() == null) {
                    requestBody = okhttp3.internal.Util.EMPTY_REQUEST;
                } else {
                    String contentType = openApi.getHeaders().containsKey("content-type")
                            ? openApi.getHeaders().get("content-type"): openApi.getHeaders().get("Content-Type");
                    if(contentType == null) {
                        throw new SendFailedException("POST 请求标头内容类型为 null");
                    }
                    MediaType mediaType = MediaType.parse(contentType);
                    requestBody = RequestBody.create(openApi.getBody(), mediaType);
                }

                log.debug("RequestBody: {}", openApi.getBody());
                req = requestBuilder.post(requestBody).build();
                break;
            case "DELETE":
                req = requestBuilder.delete().build();
                break;
            default:
                throw new SendFailedException("不支持的 HTTP method " + openApi.getHttpMethod());
        }

        return req;
    }

    /**
     * 构建 multipart 请求体
     */
    private RequestBody buildMultipartBody(OpenApi openApi) {
        FileUpload upload = openApi.getFileUpload();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        upload.uploadPart(builder);

        // 添加查询参数作为表单字段
        openApi.getQueryParam().forEach((k, v) ->
                builder.addFormDataPart(k, v.toString()));

        return builder.build();
    }

    public String execute(OpenApi openApi){
        String responseStr = "";
        try (Response response = OkHttpClientFactory.getDefaultClient().newCall(this.build(openApi)).execute()) {
            ResponseBody responseBody = response.body();
            if (response.isSuccessful()) {
                if(responseBody != null){
                    responseStr = responseBody.string();
                }
            } else {
                if(responseBody == null){
                    throw new SendFailedException(response.code() + " " + response.message());
                }

                String body = responseBody.string();
                if(!body.isEmpty()){
                    responseStr = body;
                }
            }

            log.debug("ResponseBody: {}", responseStr);
            return responseStr;
        } catch (IOException e) {
            throw new SendFailedException("网络请求失败", e);
        }
    }
}
