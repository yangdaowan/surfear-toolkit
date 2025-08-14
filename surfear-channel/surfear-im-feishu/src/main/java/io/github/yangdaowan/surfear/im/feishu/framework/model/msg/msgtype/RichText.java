package io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class RichText {

    private JSONObject post;

    public RichText(String title, List<List<RichText.ParagraphNode>> contents) {
        JSONObject post = new JSONObject();
        post.put("zh_cn", new JSONObject());
        if(title == null || title.isEmpty()) {
            throw new RuntimeException("富文本标题不能为空");
        }
        post.getJSONObject("zh_cn").put("title", title);
        if(contents == null || contents.isEmpty()) {
            throw new RuntimeException("富文本内容不能为空");
        }
        post.getJSONObject("zh_cn").put("content", contents);

        this.post = post;
    }

    public RichText(String templateContent) {
        JSONObject post = new JSONObject();
        post.put("zh_cn", JSONObject.parse(templateContent));
        this.post = post;
    }

    /**
     * 富文本段落
     * @author ycf
     **/
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Paragraph {

        private List<ParagraphNode> paragraphNodes = new ArrayList<>();

        public Paragraph addParagraphNode(ParagraphNode paragraphNode) {
            if (paragraphNode != null) {
                paragraphNodes.add(paragraphNode);
            }
            return this;
        }
    }

    /**
     * 富文本段落节点
     * @author ycf
     **/
    public abstract static class ParagraphNode {

        @EqualsAndHashCode(callSuper = true)
        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class Text extends ParagraphNode {

            private final String tag = "text";
            /**
             * 是否必填: 是<br/>
             * 描述: 文本内容，支持文本转义
             */
            private String text;

            /**
             * 是否必填: 否<br/>
             * 描述: 表示是否 unescape 解码。默认值为 false，未用到 unescape 时可以不填。
             */
            private boolean un_escape = false;

            public Text(String text) {
                this.text = text;
            }

        }

        @EqualsAndHashCode(callSuper = true)
        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class Img extends ParagraphNode {

            private final String tag = "img";

            /**
             * 是否必填: 是<br/>
             * 描述: 图片的唯一标识。可通过<a href="https://open.feishu.cn/document/server-docs/im-v1/image/create">上传图片</a> 接口获取 image_key。
             */
            private String image_key;

        }

        /**
         * 文档有问题，该功能无法正常使用
         */
        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class At extends ParagraphNode {

            private final String tag = "at";

            /**
             * 是否必填: 是<br/>
             * 描述:用户的 Open ID，User ID 不考虑使用，最好统一使用 Open ID。<br/>
             * - @ 所有人时，填 all。<br/>
             */
            private String user_id;

            /**
             * 默认@所有人。
             */
            public At() {
                this.user_id = "all";
            }

            public At(String user_id) {
                this.user_id = user_id;
            }
        }

        @EqualsAndHashCode(callSuper = true)
        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class A extends ParagraphNode {

            private final String tag = "a";

            /**
             * 是否必填: 是<br/>
             * 描述: 超链接的文本内容。
             */
            private String text;

            /**
             * 是否必填: 是<br/>
             * 描述: 默认的链接地址，你需要确保链接地址的合法性，否则消息会发送失败。
             */
            private String href;

        }
    }

}
