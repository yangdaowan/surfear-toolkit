package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ycf
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Markdown {

    /**
     * 消息标题。
     */
    private String title;

    /**
     * markdown格式的消息。
     * 标题
     * # 一级标题
     * ## 二级标题
     * ### 三级标题
     * #### 四级标题
     * ##### 五级标题
     * ###### 六级标题
     *
     * 引用
     * > A man who stands for nothing will fall for anything.
     *
     * 文字加粗、斜体
     * **bold**
     * *italic*
     *
     * 链接
     * [this is a link](http://name.com)
     *
     * 图片（建议不要超过20张）
     * ![](http://name.com/pic.jpg)
     *
     * 无序列表
     * - item1
     * - item2
     *
     * 有序列表
     * 1. item1
     * 2. item2
     */
    private String text;

    /**
     * 可以通过构造函数传入标题。再通过模板填充方式设置markdown内容。
     * @param title
     */
    public Markdown(String title) {
        this.title = title;
    }
}
