package io.github.yangdaowan.surfear.mail.smtp.core;

import javax.activation.DataHandler;
import java.io.Serializable;

/**
 * 邮件附件
 * @author ycf
 **/
public class MailAttachment implements Serializable {

    /**
     * 附件名称 xxx.xx
     */
    private String filename;
    /**
     * 附件对象 可为空
     */
    private DataHandler data;

    public MailAttachment(String filename, DataHandler data) {
        this.filename = filename;
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public DataHandler getData() {
        return data;
    }

    public void setData(DataHandler data) {
        this.data = data;
    }
}
