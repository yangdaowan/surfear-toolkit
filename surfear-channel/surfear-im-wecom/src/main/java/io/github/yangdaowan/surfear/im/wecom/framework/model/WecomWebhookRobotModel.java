package io.github.yangdaowan.surfear.im.wecom.framework.model;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.interceptor.support.required.Required;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.custom.*;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.msgtype.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @see <a href="https://developer.work.weixin.qq.com/document/path/99110#%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8%E7%BE%A4%E6%9C%BA%E5%99%A8%E4%BA%BA">群机器人配置说明</a>
 * </br>
 * - 每个机器人发送的消息不能超过20条/分钟。
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class WecomWebhookRobotModel extends Message {

    /**
     * 自定义的机器人消息
     */
    @Required
    private CustomRobotMsg msg;

    @Override
    public void setContent(String content) {
        // 文本类型
        if (msg instanceof TextMsg) {
            TextMsg textMsg = (TextMsg)msg;
            if(textMsg.getText() == null){
                textMsg.setText(new Text(content));
            } else {
                textMsg.getText().setContent(content);
            }
        }
        // markdown类型
        else if (msg instanceof MarkdownMsg ) {
            MarkdownMsg markdownMsg = (MarkdownMsg)msg;
            markdownMsg.setMarkdown(new Markdown(content));
        }
        // markdown_v2类型
        else if (msg instanceof MarkdownV2Msg ) {
            MarkdownV2Msg markdownV2Msg = (MarkdownV2Msg)msg;
            markdownV2Msg.setMarkdown_v2(new MarkdownV2(content));
        }
        // 图文类型
        else if (msg instanceof NewsMsg) {
            NewsMsg newsMsg = (NewsMsg)msg;
            newsMsg.setNews(new News(
                    JSONArray.parseArray(content, Article.class)
            ));
        }
        // 模板卡片类型
        else if (msg instanceof TemplateCardMsg) {
            TemplateCardMsg templateCardMsg = (TemplateCardMsg)msg;
            templateCardMsg.setTemplate_card(JSONObject.parse(content));
        }
        else {
            throw new RuntimeException("类型未支持模板填充");
        }
    }
}
