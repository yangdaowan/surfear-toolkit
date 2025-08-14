package io.github.yangdaowan.surfear.mail.smtp.framework;

import io.github.yangdaowan.surfear.core.spi.client.AbsMD5HexConfig;
import io.github.yangdaowan.surfear.core.spi.client.Unique;
import io.github.yangdaowan.surfear.core.spi.client.ConfigPrefix;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@ConfigPrefix("smtp-mail")
public class SmtpMailConfig extends AbsMD5HexConfig {

    /**
     * SMTP服务器用户名
     */
    @Unique
    private String user;
    /**
     * SMTP服务器密码
     */
    private String password;
    /**
     * SMTP服务器地址
     */
    private String host;
    /**
     * SMTP服务器端口
     */
    private Integer port = 587;
    /**
     * 是否使用SSL
     */
    private Boolean auth = true;
    /**
     * 是否使用STARTTLS
     */
    private Boolean starttls_enable = true;
    /**
     * 连接超时时间（毫秒）
     */
    private Long connectiontimeout = 5000L;
    /**
     * 读超时时间（毫秒）
     */
    private Long timeout = 10000L;
    /**
     * 写超时时间（毫秒）
     */
    private Long writetimeout = 10000L;

}
