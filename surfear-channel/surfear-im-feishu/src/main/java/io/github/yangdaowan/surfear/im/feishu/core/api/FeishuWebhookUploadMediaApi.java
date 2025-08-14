package io.github.yangdaowan.surfear.im.feishu.core.api;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.openapi.bean.FileUpload;
import io.github.yangdaowan.surfear.core.openapi.response.JsonResponse;
import io.github.yangdaowan.surfear.im.feishu.core.TenantAccessTokenUtil;
import io.github.yangdaowan.surfear.im.feishu.framework.config.FeishuWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.feishu.framework.exception.FeishuException;
import io.github.yangdaowan.surfear.im.feishu.framework.model.FeishuFileUpload;
import io.github.yangdaowan.surfear.im.feishu.framework.model.FeishuImageUpload;

import java.io.File;


/**
 * @see <a href="https://developer.work.weixin.qq.com/document/path/99110#%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8%E7%BE%A4%E6%9C%BA%E5%99%A8%E4%BA%BA">群机器人配置说明</a>
 * </br>
 * - 每个机器人发送的消息不能超过20条/分钟。
 * @author ycf
 **/
public class FeishuWebhookUploadMediaApi extends AbsOpenApi {

    private final FeishuWebhookRobotConfig config;

    /**
     * 仅支持 images 和 files
     */
    private final String type;
    private final File file;

    public FeishuWebhookUploadMediaApi(FeishuWebhookRobotConfig config, String type, File file) {
        this.config = config;
        this.type = type;
        this.file = file;

        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 接口信息
        this.setUrl("https://open.feishu.cn/open-apis/im/v1/" + type);
        // Header参数
        this.addHeader("Authorization", "Bearer " + TenantAccessTokenUtil.getInstance().getAccessToken(this.config));
        // 设置文件上传
        FileUpload fileUpload;
        if(type.equals("images")) {
            fileUpload = new FeishuImageUpload(file);
        } else if(type.equals("files")) {
            fileUpload = new FeishuFileUpload(file);
        } else {
            throw new IllegalArgumentException("不支持的文件类型: " + type + "，仅支持 images 和 files");
        }
        this.setMultipartFile(fileUpload);
    }

    @Override
    public JSONObject parseResponse(String body) {
        return new JsonResponse("code", "msg", "0")
                .throwException(FeishuException.class)
                .parseResponse(body);
    }
}
