package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author ycf
 **/
@AllArgsConstructor
@Data
public class ActionCard {

    /**
     * 消息标题。
     */
    private String title;
    /**
     * 消息内容。如果太长只会部分展示。
     */
    private String text;
    /**
     * 单个按钮的标题。
     */
    private String singleTitle;
    /**
     * 点击消息跳转的URL
     */
    private String singleURL;
    /**
     * 按钮。
     */
    private List<Btns> btns;
    /**
     * 按钮排列方式： <br/>
     * 0：按钮竖直排列 <br/>
     * 1：按钮横向排列
     */
    private String btnOrientation;

    /**
     * 构造函数，创建一个ActionCard对象。再通过模板填充方式设置text的markdown内容。
     *
     * @param title         消息标题
     * @param btnOrientation 按钮排列方式
     * @param singleURL     单个按钮的URL
     * @param singleTitle   单个按钮的标题
     * @param btns          按钮列表
     */
    public ActionCard(String title, String singleTitle, String singleURL, List<Btns> btns, String btnOrientation) {
        this.title = title;
        this.singleTitle = singleTitle;
        this.singleURL = singleURL;
        this.btns = btns;
        this.btnOrientation = btnOrientation;
    }
}
