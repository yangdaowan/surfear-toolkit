package io.github.yangdaowan.surfear.im.dingtalk.framework.model;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.interceptor.support.required.Required;
import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.custom.*;
import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @see <a href="https://open.dingtalk.com/document/orgapp/custom-robots-send-group-messages">自定义机器人发送群消息</a>
 * </br>
 * 每个机器人每分钟最多发送20条消息到群里，如果超过20条，会限流10分钟。
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class DingtalkWebhookRobotModel extends Message {

    /**
     * 自定义的机器人消息
     */
    @Required
    private CustomRobotMsg msg;

    @Override
    public void setContent(String content) {
        if(msg instanceof TextMsg){
            TextMsg textMsg = (TextMsg)msg;
            if(textMsg.getText() == null){
                textMsg.setText(new Text(content));
            } else {
                textMsg.getText().setContent(content);
            }
        }
        else if(msg instanceof LinkMsg){
            LinkMsg linkMsg = (LinkMsg)msg;
            linkMsg.setLink(JSONObject.parseObject(content, Link.class));
        }
        else if(msg instanceof MarkdownMsg){
            MarkdownMsg markdownMsg = (MarkdownMsg)msg;
            if(markdownMsg.getMarkdown() == null){
                Object title = this.getTemplateParams().getOrDefault("markdown_title", null);
                if(title == null || title.toString().isEmpty()){
                    throw new RuntimeException("MarkdownMsg 模板消息中，变量markdown_title参数不能为空");
                }

                markdownMsg.setMarkdown(new Markdown(title.toString(), content));
            } else {
                markdownMsg.getMarkdown().setText(content);
            }
        }
        else if(msg instanceof ActionCardMsg){
            ActionCardMsg actionCardMsg = (ActionCardMsg)msg;
            actionCardMsg.getActionCard().setText(content);
        }
        else if (msg instanceof FeedCardMsg) {
            FeedCardMsg feedCardMsg = (FeedCardMsg) msg;
            feedCardMsg.setFeedCard(new FeedCard(JSONArray.parseArray(content, Links.class)));

        } else {
            throw new RuntimeException("类型未支持模板填充");
        }
    }
}
