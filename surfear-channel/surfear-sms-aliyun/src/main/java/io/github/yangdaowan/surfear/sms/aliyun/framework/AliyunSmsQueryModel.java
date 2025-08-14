package io.github.yangdaowan.surfear.sms.aliyun.framework;

import io.github.yangdaowan.surfear.core.interceptor.support.required.Required;
import lombok.Data;

/**
 * @author ycf
 **/
@Data
public class AliyunSmsQueryModel {

    /**
     * 需要查询的手机号码。格式：
     *
     * 国内短信：11 位手机号码，例如 1390000****。
     * 国际/港澳台消息：国际区号+号码，例如 8520000****。
     */
    @Required
    private String PhoneNumber;

    /**
     * 短信发送日期，支持查询最近 30 天的记录。
     *
     * 格式：yyyyMMdd，例如 20250601。
     *
     * 示例值:
     * 20250601
     */
    @Required
    private String SendDate;

    /**
     * 每页显示的短信记录数量，供分页查看发送记录使用。
     *
     * 取值范围为 1~50。
     *
     * 注意 该字段类型为 Long，在序列化/反序列化的过程中可能导致精度丢失，请注意数值不得大于 9007199254740991。
     * 示例值:
     * 20
     */
    @Required
    private Integer PageSize;

    /**
     * 发送记录的当前页码，供分页查看发送记录使用。
     *
     * 注意 该字段类型为 Long，在序列化/反序列化的过程中可能导致精度丢失，请注意数值不得大于 9007199254740991。
     * 示例值:
     * 1
     */
    @Required
    private Integer CurrentPage;

    /**
     * 发送回执 ID。即发送流水号，调用 SendSms 或 SendBatchSms 发送短信时，返回值中的 BizId 字段。
     */
    private String BizId;

}
