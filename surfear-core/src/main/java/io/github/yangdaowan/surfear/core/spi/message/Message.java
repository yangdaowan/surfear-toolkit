package io.github.yangdaowan.surfear.core.spi.message;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;

/**
 * 消息模型
 * @author ycf
 **/
@Slf4j
@Data
public abstract class Message implements Serializable {
    /**
     * 接收人
     */
    private String to;
    /**
     * 内容
     */
    private String content;
    /**
     * 模板文件名（可选）
     */
    private String templateFileName;
    /**
     * 模板参数
     */
    private Map<String, Object> templateParams;

    public void setContent(String content) {
        this.content = content;

        log.info("向 [{}] 发送：\r\n{}", formatTo(this.getTo()), formatContent(content));
    }

    public String formatTo(String to){
        if(to == null){
            return "";
        }
        int i = to.indexOf(",");
        if(i != -1){
            // 如果有逗号，表示有多个接收人，截取第一个接收人并显示总数
            return to.substring(0, i) + "...等" + to.split(",").length+"个用户";
        }
        if(to.length() <= 20){
            return to;
        }
        return to.substring(0, 20) + "...";
    }

    public String formatContent(String content){
        if(content == null){
            return "";
        }
        if(content.length() <= 100){
            return content;
        }
        return content.substring(0, 100) + "...";
    }
}
