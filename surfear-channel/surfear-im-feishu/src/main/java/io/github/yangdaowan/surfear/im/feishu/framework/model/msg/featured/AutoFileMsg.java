package io.github.yangdaowan.surfear.im.feishu.framework.model.msg.featured;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.client.OkhttpClient;
import io.github.yangdaowan.surfear.core.spi.client.Config;
import io.github.yangdaowan.surfear.im.feishu.core.api.FeishuWebhookUploadMediaApi;
import io.github.yangdaowan.surfear.im.feishu.framework.config.FeishuWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom.CustomRobotMsg;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom.ImageMsg;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype.Image;

import java.io.File;

/**
 * @author ycf
 **/
public class AutoFileMsg extends CustomRobotMsg implements Auto<ImageMsg> {

    private File file;

    public AutoFileMsg(File file) {
        if (file == null) {
            throw new IllegalArgumentException("文件不能为空！");
        }
        this.file = file;
    }

    @Override
    public ImageMsg auto(Config config) {
        FeishuWebhookUploadMediaApi uploadMediaApi = new FeishuWebhookUploadMediaApi((FeishuWebhookRobotConfig) config, "files", file);
        JSONObject response = uploadMediaApi.parseResponse(new OkhttpClient().execute(uploadMediaApi));
        String fileKey = response.getJSONObject("data").getString("file_key");
        return new ImageMsg(new Image(fileKey));
    }
}
