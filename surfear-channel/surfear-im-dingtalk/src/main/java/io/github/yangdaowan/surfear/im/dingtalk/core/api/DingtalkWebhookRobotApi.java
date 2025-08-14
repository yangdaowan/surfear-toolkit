package io.github.yangdaowan.surfear.im.dingtalk.core.api;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.config.ConfigUtil;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.openapi.response.JsonResponse;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.im.dingtalk.framework.config.DingtalkWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.dingtalk.framework.exception.DingtalkException;
import io.github.yangdaowan.surfear.im.dingtalk.framework.model.DingtalkWebhookRobotModel;
import io.github.yangdaowan.surfear.im.dingtalk.utils.WebhookRobotSignUtil;


/**
 * @see <a href="https://open.dingtalk.com/document/orgapp/custom-robots-send-group-messages">自定义机器人发送群消息</a>
 * @author ycf
 **/
public class DingtalkWebhookRobotApi extends AbsOpenApi {

    private final MessageContext context;
    private final DingtalkWebhookRobotConfig config;
    private final DingtalkWebhookRobotModel model;

    public DingtalkWebhookRobotApi(MessageContext context) {
        this.context = context;
        this.model = (DingtalkWebhookRobotModel) context.getModel();
        this.config = ConfigUtil.getChannelConfigObject(context.getConfig(), DingtalkWebhookRobotConfig.class);

        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 接口信息
        this.setPostJson("https://oapi.dingtalk.com/robot/send");
        // Query参数
        this.putQuery("access_token", config.getAccessToken());
        if(config.getSecretKey() != null && !config.getSecretKey().isEmpty()) {
            Long timestamp = System.currentTimeMillis();
            String sign = WebhookRobotSignUtil.sign(timestamp, config.getSecretKey());
            this.putQuery("timestamp", String.valueOf(timestamp));
            this.putQuery("sign", sign);
        }
        // Body参数
        this.setBody(JSON.toJSONString(model.getMsg()));
    }

    @Override
    public JSONObject parseResponse(String body) {
        return new JsonResponse("errcode", "errmsg", "0")
                .throwException(DingtalkException.class)
                .parseResponse(body);
    }
}
