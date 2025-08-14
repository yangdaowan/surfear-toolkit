package io.github.yangdaowan.surfear.core.interceptor.support.template.model;

/**
 * @author ycf
 **/
public class MessageTemplate {

    /**
     * TemplateSource 枚举 name() 小写
     */
    private String source;
    /**
     * MessageType 枚举 name() 小写
     */
    private String messageType;
    /**
     * TemplateMode 枚举 name() 小写
     */
    private String mode;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板内容
     */
    private String content;

    public MessageTemplate() {
    }

    public MessageTemplate(String messageType, String mode, String name, String content) {
        this.messageType = messageType;
        this.mode = mode;
        this.name = name;
        this.content = content;
    }

    public MessageTemplate(String source, String messageType, String mode, String name, String content) {
        this.source = source;
        this.messageType = messageType;
        this.mode = mode;
        this.name = name;
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMode() {
        return mode;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "MessageTemplate{" +
                "source='" + source + '\'' +
                ", messageType='" + messageType + '\'' +
                ", mode='" + mode + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
