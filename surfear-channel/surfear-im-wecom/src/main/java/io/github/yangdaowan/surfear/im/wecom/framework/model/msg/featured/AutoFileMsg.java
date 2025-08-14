package io.github.yangdaowan.surfear.im.wecom.framework.model.msg.featured;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.client.OkhttpClient;
import io.github.yangdaowan.surfear.core.spi.client.Config;
import io.github.yangdaowan.surfear.im.wecom.core.api.WecomWebhookUploadMediaApi;
import io.github.yangdaowan.surfear.im.wecom.framework.config.WecomWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.custom.CustomRobotMsg;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.custom.FileMsg;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.msgtype.MediaFile;

import java.io.File;

/**
 * @author ycf
 **/
public class AutoFileMsg extends CustomRobotMsg implements Auto<FileMsg> {

    private File file;

    public AutoFileMsg(File file) {
        if (file == null) {
            throw new IllegalArgumentException("文件不能为空！");
        }
        this.file = file;
    }

    @Override
    public FileMsg auto(Config config) {
        WecomWebhookUploadMediaApi uploadMediaApi = new WecomWebhookUploadMediaApi((WecomWebhookRobotConfig) config, "file", file);
        JSONObject response = uploadMediaApi.parseResponse(new OkhttpClient().execute(uploadMediaApi));
        String mediaId = response.getString("media_id");
        return new FileMsg(new MediaFile(mediaId));
    }
}
