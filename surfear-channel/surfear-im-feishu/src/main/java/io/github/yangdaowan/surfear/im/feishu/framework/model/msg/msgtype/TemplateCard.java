package io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemplateCard {

    private final String type = "template";
    /**
     * TemplateCardData 类的字段内容需要先在 Feishu 平台上配置好，使用搭建工具创建并发布卡片<br>
     * <a href="https://open.feishu.cn/document/feishu-cards/quick-start/send-message-cards-with-custom-bot#bad5a929">方式一：使用搭建工具创建并发布卡片</a>
      */
    private TemplateCardData data;

}
