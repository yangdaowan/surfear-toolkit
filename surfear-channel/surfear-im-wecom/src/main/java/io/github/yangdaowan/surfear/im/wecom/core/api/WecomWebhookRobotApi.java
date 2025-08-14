package io.github.yangdaowan.surfear.im.wecom.core.api;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.config.ConfigUtil;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.openapi.response.JsonResponse;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.im.wecom.framework.config.WecomWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.wecom.framework.exception.WecomException;
import io.github.yangdaowan.surfear.im.wecom.framework.model.WecomWebhookRobotModel;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.custom.CustomRobotMsg;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.featured.Auto;


/**
 * @see <a href="https://developer.work.weixin.qq.com/document/path/99110#%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8%E7%BE%A4%E6%9C%BA%E5%99%A8%E4%BA%BA">群机器人配置说明</a>
 * </br>
 * - 每个机器人发送的消息不能超过20条/分钟。
 * @author ycf
 **/
public class WecomWebhookRobotApi extends AbsOpenApi {

    private final MessageContext context;
    private final WecomWebhookRobotConfig config;
    private final WecomWebhookRobotModel model;

    public WecomWebhookRobotApi(MessageContext context) {
        this.context = context;
        this.model = (WecomWebhookRobotModel) context.getModel();
        this.config = ConfigUtil.getChannelConfigObject(context.getConfig(), WecomWebhookRobotConfig.class);

        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 特色功能
        this.featuredFeatures();
        // 接口信息
        this.setPostJson("https://qyapi.weixin.qq.com/cgi-bin/webhook/send");
        // Query参数
        this.putQuery("key", config.getAccessToken());
        // Body参数
        JSONObject body = JSONObject.from(model.getMsg());
        this.setBody(body.toJSONString());
    }

    @Override
    public JSONObject parseResponse(String body) {
        return new JsonResponse("errcode", "errmsg", "0")
                .throwException(WecomException.class)
                .parseResponse(body);
    }

    private void featuredFeatures() {
        CustomRobotMsg msg = model.getMsg();
        if (msg instanceof Auto) {
            model.setMsg(((Auto<?>) msg).auto(config));
        }
    }

}
