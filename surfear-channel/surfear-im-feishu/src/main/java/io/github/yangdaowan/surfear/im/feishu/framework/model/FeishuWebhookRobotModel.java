package io.github.yangdaowan.surfear.im.feishu.framework.model;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.interceptor.support.required.Required;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom.CustomCardMsg;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom.CustomRobotMsg;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom.RichTextMsg;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom.TextMsg;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype.RichText;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype.Text;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @see <a href="https://open.feishu.cn/document/client-docs/bot-v3/add-custom-bot#478cb64f">自定义机器人使用指南</a>
 * </br>
 * - 自定义机器人的频率控制和普通应用不同，为单租户单机器人 100 次/分钟，5 次/秒。建议发送消息尽量避开诸如 10:00、17:30 等整点及半点时间，否则可能出现因系统压力导致的 11232 限流错误，导致消息发送失败。<br/>
 * - 发送消息时，请求体的数据大小不能超过 20 KB。
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class FeishuWebhookRobotModel extends Message {

    /**
     * 自定义的机器人消息
     */
    @Required
    private CustomRobotMsg msg;

    @Override
    public void setContent(String content) {
        if (msg instanceof TextMsg) {
            TextMsg textMsg = (TextMsg)msg;
            if(textMsg.getContent() == null){
                textMsg.setContent(new Text(content));
            } else {
                textMsg.getContent().setText(content);
            }
        }
        else if (msg instanceof RichTextMsg) {
            RichTextMsg richTextMsg = (RichTextMsg) msg;
            richTextMsg.setContent(new RichText(content));
        }
        else if (msg instanceof CustomCardMsg) {
            CustomCardMsg customCardMsg = (CustomCardMsg) msg;
            // 第一种，自定义卡片不支持变量
            customCardMsg.setCard(JSONObject.parseObject(content));
        } else {
            throw new RuntimeException("类型未支持模板填充");
        }
    }
}
