package io.github.yangdaowan.surfear.im.wecom.framework.model.msg.featured;

import io.github.yangdaowan.surfear.core.spi.client.Config;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.custom.CustomRobotMsg;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.custom.ImageMsg;
import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.msgtype.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author ycf
 **/
public class AutoImageMsg extends CustomRobotMsg implements Auto<ImageMsg> {

    private File file;

    public AutoImageMsg(File file) {
        if (file == null) {
            throw new IllegalArgumentException("文件不能为空！");
        }
        this.file = file;
    }

    @Override
    public ImageMsg auto(Config config) {
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());

            // 计算MD5
            String md5 = calculateMD5(fileBytes);

            // Base64编码
            String base64 = Base64.getEncoder().encodeToString(fileBytes);

            return new ImageMsg(new Image(base64, md5));
        } catch (IOException e) {
            throw new RuntimeException("无法处理图像文件： " + file.getName(), e);
        }
    }

    /**
     * 计算字节数组的MD5值
     */
    public static String calculateMD5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("无法计算 MD5", e);
        }
    }
}
