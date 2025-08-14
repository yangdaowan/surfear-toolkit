package io.github.yangdaowan.surfear.im.wecom.framework.model.msg.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ycf
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class News {

    private List<Article> articles;

    public News(Article article) {
        this.articles = new ArrayList<>();
        this.articles.add(article);
    }

    public News addArticle(Article article) {
        if (this.articles == null) {
            this.articles = new ArrayList<>();
        }
        this.articles.add(article);
        return this;
    }
}
