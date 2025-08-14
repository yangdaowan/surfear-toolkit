package io.github.yangdaowan.surfear.im.wecom.core.api;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.openapi.response.JsonResponse;
import io.github.yangdaowan.surfear.im.wecom.framework.config.WecomWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.wecom.framework.exception.WecomException;
import io.github.yangdaowan.surfear.im.wecom.framework.model.WecomFileUpload;

import java.io.File;


/**
 * @see <a href="https://developer.work.weixin.qq.com/document/path/99110#%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8%E7%BE%A4%E6%9C%BA%E5%99%A8%E4%BA%BA">群机器人配置说明</a>
 * </br>
 * - 每个机器人发送的消息不能超过20条/分钟。
 * @author ycf
 **/
public class WecomWebhookUploadMediaApi extends AbsOpenApi {

    private final WecomWebhookRobotConfig config;

    private final String type;
    private final File file;


    public WecomWebhookUploadMediaApi(WecomWebhookRobotConfig config, String type, File file) {
        this.config = config;
        this.type = type;
        this.file = file;

        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 接口信息
        this.setUrl("https://qyapi.weixin.qq.com/cgi-bin/webhook/upload_media");
        // Query参数
        this.putQuery("key", config.getAccessToken());
        this.putQuery("type", type);
        // 设置文件上传
        this.setMultipartFile(new WecomFileUpload(file));
    }

    @Override
    public JSONObject parseResponse(String body) {
        return new JsonResponse("errcode", "errmsg", "0")
                .throwException(WecomException.class)
                .parseResponse(body);
    }
}
