package io.github.yangdaowan.surfear.im.wecom.framework.model.msg.featured;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.client.OkhttpClient;
import io.github.yangdaowan.surfear.core.spi.client.Config;
import io.github.yangdaowan.surfear.im.wecom.core.api.WecomWebhookUploadMediaApi;
import io.github.yangdaowan.surfear.im.wecom.framework.config.WecomWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.custom.CustomRobotMsg;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.custom.VoiceMsg;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.msgtype.Voice;

import java.io.File;

/**
 * @author ycf
 **/
public class AutoVoiceMsg extends CustomRobotMsg implements Auto<VoiceMsg> {

    private File file;

    public AutoVoiceMsg(File file) {
        if (file == null) {
            throw new IllegalArgumentException("文件不能为空！");
        }
        this.file = file;
    }

    @Override
    public VoiceMsg auto(Config config) {
        WecomWebhookUploadMediaApi uploadMediaApi = new WecomWebhookUploadMediaApi((WecomWebhookRobotConfig) config, "voice", file);
        JSONObject response = uploadMediaApi.parseResponse(new OkhttpClient().execute(uploadMediaApi));
        String mediaId = response.getString("media_id");
        return new VoiceMsg(new Voice(mediaId));
    }
}
