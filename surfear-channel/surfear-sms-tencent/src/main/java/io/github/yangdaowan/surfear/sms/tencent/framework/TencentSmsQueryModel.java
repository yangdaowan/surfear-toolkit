package io.github.yangdaowan.surfear.sms.tencent.framework;

import io.github.yangdaowan.surfear.core.interceptor.support.required.Required;
import lombok.Data;

/**
 * @author ycf
 **/
@Data
public class TencentSmsQueryModel {

    /**
     * 下发目的手机号码，依据 E.164 标准为：+[国家（或地区）码][手机号] ，示例如：+8618501234444， 其中前面有一个+号 ，86为国家码，18501234444为手机号。
     * 示例值：+8618501234444
     */
    @Required
    private String PhoneNumber;

    /**
     * 拉取起始时间，UNIX 时间戳（时间：秒）。
     * 注：最大可拉取当前时期前7天的数据。
     * 示例值：1464624000
     */
    @Required
    private Long BeginTime;

    /**
     * 拉取截止时间，UNIX 时间戳（时间：秒）。
     * 示例值：1464624123
     */
    private Long EndTime;

    /**
     * 偏移量。
     * 注：目前固定设置为0。
     * 示例值：0
     */
    @Required
    private Integer Offset;

    /**
     * 拉取最大条数，最多 100。
     * 示例值：2
     */
    @Required
    private Integer Limit;

    /**
     * 短信 SdkAppId 在 短信控制台 添加应用后生成的实际 SdkAppId，示例如1400006666。
     * 示例值：1400006666
     */
    @Required
    private String SmsSdkAppId;

}
