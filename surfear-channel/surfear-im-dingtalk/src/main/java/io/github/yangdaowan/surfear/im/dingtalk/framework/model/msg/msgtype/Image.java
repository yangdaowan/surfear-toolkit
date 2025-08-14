package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype;

/**
 * @author ycf
 **/
public class Image {

    private String photoURL;

    public Image(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
