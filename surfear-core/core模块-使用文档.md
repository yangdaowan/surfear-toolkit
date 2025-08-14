# Surfear Core - 使用文档

## 1. 功能介绍

Surfear Core 是消息推送工具包的核心模块，提供了统一的消息处理框架、SPI扩展机制、配置管理、拦截器链等基础功能。

### 1.1 主要功能

- SPI扩展机制
    - 消息通道定义
    - 消息模型定义
    - 配置客户端定义
- 配置管理
    - 统一的配置加载和管理
    - 提供多级配置向下覆盖策略：临时配置 > 环境变量 > 项目配置 > 默认配置
- 拦截器链
    - 支持消息处理过程的自定义拦截和处理
        - 日志打印
            - 结果日志
            - 异常日志
        - 参数检查
            - 消息模型校验
        - 模板支持
            - 模板引擎集成
            - 模板变量替换
            - 模板缓存
- 线程池管理
    - 线程池工厂，统一管理线程资源
- OpenAPI集成
    - openapi请求定义
    - 请求客户端定义
    - 短信反馈服务定义
- 异常处理
    - 完整的异常体系

## 2. 核心概念

### 2.1 通道（Channel）

通道是消息发送的具体实现，通过SPI机制加载：

```java
@AutoService(MessageChannel.class)
@Channel(
        type = MessageType.SMS,
        name = "aliyun",
        description = "阿里云短信",
        model = AliyunSmsModel.class,
        config = AliyunConfig.class,
        exception = AliyunException.class
)
public class AliyunSmsChannel extends AbsMessageChannel {

    @Override
    public MessageResult send(MessageContext context) {
        AliyunSendSmsApi api = new AliyunSendSmsApi(context);
        return MessageResult.success(context, api.parseResponse(new OkhttpClient().execute(api)));
    }

}
```

### 2.2 拦截器（Interceptor）

拦截器用于处理消息发送过程中的通用逻辑：

```java
@Order(1)
public class LoggingInterceptor implements MessageInterceptor {
    @Override
    public MessageResult intercept(MessageChain chain) {
        // 前置处理
        MessageResult result = chain.proceed();
        // 后置处理
        return result;
    }
}
```

### 3.3 配置（Config）

支持多种配置方式：临时配置 > 环境变量 > 项目配置 > 默认配置

**配置对象（默认配置）**
```java
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
```
**临时配置**
```java
@Test
public void test() {
    SmtpMailModel model = new SmtpMailModel();
    model.setTo("1114389297@qq.com");
    model.setSubject("测试邮件-纯文本");
    model.setContent("这是一封由 surfear 发送的，纯文本邮件");

    // 临时配置
    Properties config = new Properties();
    config.put("surfear.smtp-mail.host", "smtp.qq.com");
    config.put("surfear.smtp-mail.user", "xxx@qq.com");
    config.put("surfear.smtp-mail.password", "xxxx");
    
    // 传递生效
    MessageSender.send("mail:smtp", model, config);
}
```
**环境变量**
```java
// 环境变量
// IDE（如IntelliJ/Eclipse）需要重启才能获取新的环境变量
@Test
public void testEv() throws InstantiationException, IllegalAccessException {
    String ev = ConfigConverter.formatEnvironmentVariable("mail:smtp");
    System.out.println(ev);
}
```
```
// 日志打印
SURFEAR_SMTP-MAIL_HOST=;
SURFEAR_SMTP-MAIL_TIMEOUT=10000;
SURFEAR_SMTP-MAIL_PORT=587;
SURFEAR_SMTP-MAIL_AUTH=true;
SURFEAR_SMTP-MAIL_USER=;
SURFEAR_SMTP-MAIL_WRITETIMEOUT=10000;
SURFEAR_SMTP-MAIL_STARTTLS_ENABLE=true;
SURFEAR_SMTP-MAIL_PASSWORD=;
SURFEAR_SMTP-MAIL_CONNECTIONTIMEOUT=5000;
```
**项目配置（Properties配置）**
```java
@Test
public void testProperties() throws InstantiationException, IllegalAccessException {
    Properties config = ConfigConverter.convertToProperties("mail:smtp");
    config.forEach((k,v) ->{
        System.out.println(k+ "=" + v);
    });
}
```
```properties
# 日志打印
surfear.smtp-mail.host=
surfear.smtp-mail.user=
surfear.smtp-mail.password=
surfear.smtp-mail.connectiontimeout=5000
surfear.smtp-mail.starttls.enable=true
surfear.smtp-mail.writetimeout=10000
surfear.smtp-mail.auth=true
surfear.smtp-mail.timeout=10000
surfear.smtp-mail.port=587
```
**项目配置（YAML配置）**
```java
@Test
public void testYaml() throws InstantiationException, IllegalAccessException {
    Properties config = ConfigConverter.convertToProperties("mail:smtp");
    String yaml = YamlConverter.propertiesToYaml(config);
    System.out.println(yaml);
}
```
```yaml
# 日志打印
surfear:
  smtp-mail:
    host: ''
    password: ''
    user: ''
    connectiontimeout: '5000'
    starttls:
      enable: 'true'
    writetimeout: '10000'
    auth: 'true'
    timeout: '10000'
    port: '587'
```
## 4. 扩展机制

### 4.1 新增通道

1. 实现 MessageChannel 接口
2. 添加 @Channel 注解
3. @AutoService(MessageChannel.class) 自动在 META-INF/services 中注册

### 4.2 添加拦截器

1. 实现 MessageInterceptor 接口
2. 添加 @Order 注解指定顺序
3. 在 META-INF/services 中注册

### 4.3 添加消息模型
```java
public class NewMessageModel extends Message {

    // 必填
    @Required
    private String name;

    // 非必填
    private String phoneNumber;
}
```

### 4.4 添加配置模型
```java
@ConfigPrefix("xxx.xxx")
public class SmtpMailConfig extends AbsMD5HexConfig {

    // 标识唯一配置项
    @Unique
    private String name;
}
```

## 5. 线程池管理

### 5.1 默认线程池

```java
// 获取默认线程池
ExecutorService executor = ExecutorFactory.getDefaultExecutor();

// 获取调度线程池
ScheduledExecutorService scheduler = ExecutorFactory.getScheduledExecutor();
```

### 5.2 自定义线程池

```java
// 创建专用线程池
ThreadPoolExecutor customExecutor = ExecutorFactory.createChannelExecutor(
    "custom-pool",
    Runtime.getRuntime().availableProcessors(),
    Runtime.getRuntime().availableProcessors() * 2,
    1000
);
```

## 6. 异常处理

### 6.1 异常体系

```
Exception
  ├── ConfigException
  ├── ChannelLoadException
  ├── MessageException
  │   └── MessageChannelException
  ├── ModelParamValidateException
  ├── SendFailedException
  ├── SignException
  └── TemplateException
```

### 6.2 异常处理示例

```java
try {
    MessageResult result = MessageSender.send("mail:smtp", model, config);
    if(!result.isSuccess()){
        throw result.getError();
    }
} catch (SendFailedException e) {
    // 处理发送失败
} catch (ConfigException e) {
    // 处理配置错误
} catch (MessageException e) {
    // 处理其他业务异常
}
```