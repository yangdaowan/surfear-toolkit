package io.github.yangdaowan.surfear.im.feishu.core.api;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.config.ConfigUtil;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.openapi.response.JsonResponse;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.im.feishu.framework.config.FeishuWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.feishu.framework.exception.FeishuException;
import io.github.yangdaowan.surfear.im.feishu.framework.model.FeishuWebhookRobotModel;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom.CustomRobotMsg;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.featured.Auto;
import io.github.yangdaowan.surfear.im.feishu.utils.WebhookRobotSignUtil;


/**
 * @see <a href="https://open.feishu.cn/document/client-docs/bot-v3/add-custom-bot#478cb64f">自定义机器人使用指南</a>
 * </br>
 * - 自定义机器人的频率控制和普通应用不同，为单租户单机器人 100 次/分钟，5 次/秒。建议发送消息尽量避开诸如 10:00、17:30 等整点及半点时间，否则可能出现因系统压力导致的 11232 限流错误，导致消息发送失败。<br/>
 * - 发送消息时，请求体的数据大小不能超过 20 KB。
 * @author ycf
 **/
public class FeishuWebhookRobotApi extends AbsOpenApi {

    private final MessageContext context;
    private final FeishuWebhookRobotConfig config;
    private final FeishuWebhookRobotModel model;

    public FeishuWebhookRobotApi(MessageContext context) {
        this.context = context;
        this.model = (FeishuWebhookRobotModel) context.getModel();
        this.config = ConfigUtil.getChannelConfigObject(context.getConfig(), FeishuWebhookRobotConfig.class);

        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 特色功能
        this.featuredFeatures();
        // 接口信息
        this.setPostJson("https://open.feishu.cn/open-apis/bot/v2/hook/" + config.getAccessToken());
        // Body参数
        JSONObject body = JSONObject.from(model.getMsg());
        // 是否加签
        if(config.getSecretKey() != null && !config.getSecretKey().isEmpty()) {
            int timestamp = Math.toIntExact(System.currentTimeMillis() / 1000); // 飞书的时间戳单位是秒
            String sign = WebhookRobotSignUtil.sign(timestamp, config.getSecretKey());
            body.put("timestamp", String.valueOf(timestamp));
            body.put("sign", sign);
        }
        this.setBody(body.toJSONString());
    }

    @Override
    public JSONObject parseResponse(String body) {
        return new JsonResponse("code", "msg", "0")
                .throwException(FeishuException.class)
                .parseResponse(body);
    }

    private void featuredFeatures() {
        CustomRobotMsg msg = model.getMsg();
        if (msg instanceof Auto) {
            model.setMsg(((Auto<?>) msg).auto(config));
        }
    }
}
