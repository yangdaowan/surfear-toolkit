package io.github.yangdaowan.surfear.im.dingtalk.core.api.base;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.openapi.response.JsonResponse;
import io.github.yangdaowan.surfear.im.dingtalk.core.DingtalkAccessTokenUtil;
import io.github.yangdaowan.surfear.im.dingtalk.framework.config.DingtalkWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.dingtalk.framework.exception.DingtalkException;
import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.DingtalkFileUpload;

import java.io.File;


/**
 * @see <a href="https://open.dingtalk.com/document/orgapp/upload-media-files?spm=ding_open_doc.document.0.0.ae8236654gItec#ace0a0a06elxg">上传媒体文件</a>
 * </br>
 * @author ycf
 **/
public class DingtalkWebhookUploadMediaApi extends AbsOpenApi {

    private final DingtalkWebhookRobotConfig config;

    /**
     * 支持 image、voice、video、file
     */
    private final String type;
    private final File file;

    public DingtalkWebhookUploadMediaApi(DingtalkWebhookRobotConfig config, String type, File file) {
        this.config = config;
        this.type = type;
        this.file = file;

        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 接口信息
        this.setUrl("https://oapi.dingtalk.com/media/upload");
        // Query参数
        this.putQuery("access_token", DingtalkAccessTokenUtil.getInstance().getAccessToken(this.config));
        // 设置文件上传
        this.setMultipartFile(new DingtalkFileUpload(type, file));
    }

    @Override
    public JSONObject parseResponse(String body) {
        return new JsonResponse("errcode", "errmsg", "0")
                .throwException(DingtalkException.class)
                .parseResponse(body);
    }
}
