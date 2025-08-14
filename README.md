# 🌪️️ 顺风耳 · surfear
> _"三界传音，无远弗届"_ ——《封神演义》第十七回  

[![License](https://img.shields.io/badge/license-Apache2.0-blue.svg)](LICENSE)
[![Maven Central](https://img.shields.io/badge/Maven%20Central-1.0.0-brightgreen.svg)](https://search.maven.org/search?q=g:com.github.surfear)
[![Java Version](https://img.shields.io/badge/JDK-8+-green.svg)](https://www.oracle.com/java/technologies/javase-downloads.html)

## 项目概览
Surfear-Toolkit 是一个基于Java 8的统一消息通知便捷工具包，支持多种通信渠道：邮件(SMTP)、短信(阿里云/腾讯云/百度云)、IM(钉钉/飞书/企微)、推送(SSE)等。

### 项目优点

#### 1. 架构设计优秀
- 模块化设计：清晰的Maven多模块结构，职责分离明确
- SPI扩展机制：基于Java SPI + AutoService的插件化架构，扩展性极佳
- 统一API接口：MessageSender.send(channelKey, model) 提供简洁一致的调用方式
- 拦截器链模式：支持日志、参数校验、模板处理等横切关注点

#### 2. 配置管理先进
- 多级配置覆盖：临时配置 > 环境变量 > 项目配置 > 默认配置
- 类型安全配置：使用强类型配置类而非字符串配置
- 支持多格式：同时支持Properties和YAML配置文件

#### 3. 功能特性丰富
- 模板引擎集成：基于Thymeleaf支持多种模板格式（txt/html/md/json）
- 异步支持：提供CompletableFuture异步发送能力
- 安全特性：SSE模块有认证、CORS、连接限制等安全机制
- 限流保护：实现了滑动窗口限流器
- 缓存机制：Access Token自动缓存与刷新

#### 4. 代码质量良好
- 设计模式运用：工厂模式、模板方法模式、责任链模式运用恰当
- 异常体系完善：定义了完整的异常层次结构
- 线程安全考虑：使用ConcurrentHashMap、原子类等保证并发安全
- 资源管理：有临时文件清理、连接池管理等机制
- 连接池管理：HTTP连接池和线程池统一管理
- 文档完善：核心类已添加详细的JavaDoc文档

### 技术选型
- 语言：Java 8
- 核心网络：OkHttp3、Netty
- 插件管理：Java SPI
- 日志：slf4j + logback
- 测试：JUnit5 + Mockito
- 发布：Maven Central
- 文档：javadoc + markdown

## 依赖说明

项目使用最小依赖设计：

1. **Thymeleaf**: 版本2.0.43，提供模板功能。扩展`txt格式`模板中`${}`格式直接支持
2. **FastJSON2**: 版本2.0.43，提供JSON序列化与反序列化
3. **SLF4J API**: 版本1.7.36，提供日志抽象接口

消息类型依赖必要的包：
- 邮件 **JavaMail**:版本1.6.2，提供SMTP协议邮件发送
- 短信 **OkHttp**:版本4.12.0，对接提供商使用纯OpenApi请求，不依赖服务商SDK
- 站内信 **Netty**: 版本4.2.0.Final，提供SSE服务

## 🚀 快速开始

### Maven 依赖

根据您的需求选择合适的依赖：

```xml
<properties>
	<surfear.version>1.0.0</surfear.version>
</properties>

<!-- 使用完整的顺风耳消息工具包 -->
<dependency>
    <groupId>io.github.yangdaowan</groupId>
    <artifactId>surfear-message-all</artifactId>
    <version>${surfear.version}</version>
    <type>pom</type>
</dependency>

<!-- 只使用邮件通道 -->
<dependency>
    <groupId>io.github.yangdaowan</groupId>
    <artifactId>surfear-mail-smtp</artifactId>
</dependency>

<!-- 更多通道请查看相关文档 -->
```

### 使用示例

#### 发送邮件

```java
@Test
public void test() {
    SmtpMailModel model = new SmtpMailModel();
    model.setTo("QQ号@qq.com");
    model.setSubject("测试邮件-纯文本");
    model.setContent("这是一封由 "+ MessageConstant.base +" 发送的，纯文本邮件");

    MessageResult result = MessageSender.send("mail:smtp", model);
    if(!result.isSuccess()){
        throw result.getError();
    }
}
```
### 更多使用方式请参考文档：
[core模块-使用文档.md](surfear-core/core%E6%A8%A1%E5%9D%97-%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.md)

## 📋 配置示例
项目配置文件名 surfear.properties / surfear.yaml，放置在 `src/main/resources` 目录下。

### 邮件配置

```properties
surfear.smtp-mail.host=
surfear.smtp-mail.user=
surfear.smtp-mail.password=
```
```yaml
surfear:
  smtp-mail:
    host: ''
    password: ''
    user: ''
```
具体配置项请参考各个通道的配置对象。

## 📊 实现通道

| 通道名称                      | 通道描述            | 状态    |
|---------------------------|-----------------|-------|
| mail:smtp                 | [SMTP邮件-使用文档.md](surfear-channel/surfear-mail-smtp/SMTP%E9%82%AE%E4%BB%B6-%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.md)        | ✅ 已实现 |
| sms:aliyun                | [阿里云短信-使用文档.md](surfear-channel/surfear-sms-aliyun/%E9%98%BF%E9%87%8C%E4%BA%91%E7%9F%AD%E4%BF%A1-%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.md)           | ✅ 已实现 |
| sms:tencent               | [百度短信-使用文档.md](surfear-channel/surfear-sms-baidu/%E7%99%BE%E5%BA%A6%E7%9F%AD%E4%BF%A1-%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.md)           | ✅ 已实现 |
| sms:baidu                 | [腾讯云短信-使用文档.md](surfear-channel/surfear-sms-tencent/%E8%85%BE%E8%AE%AF%E4%BA%91%E7%9F%AD%E4%BF%A1-%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.md)           | ✅ 已实现 |
| im:dingtalk:webhook:robot | [钉钉webhook自定义机器人-使用文档.md](surfear-channel/surfear-im-dingtalk/%E9%92%89%E9%92%89webhook%E8%87%AA%E5%AE%9A%E4%B9%89%E6%9C%BA%E5%99%A8%E4%BA%BA-%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.md) | ✅ 已实现 |
| im:feishu:webhook:robot   | [飞书webhook自定义机器人-使用文档.md](surfear-channel/surfear-im-feishu/%E9%A3%9E%E4%B9%A6webhook%E8%87%AA%E5%AE%9A%E4%B9%89%E6%9C%BA%E5%99%A8%E4%BA%BA-%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.md) | ✅ 已实现 |
| im:wecom:webhook:robot    | [企微webhook自定义机器人-使用文档.md](surfear-channel/surfear-im-wecom/%E4%BC%81%E5%BE%AEwebhook%E8%87%AA%E5%AE%9A%E4%B9%89%E6%9C%BA%E5%99%A8%E4%BA%BA-%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.md) | ✅ 已实现 |
| push:sse                  | [SSE推送-使用文档.md](surfear-channel/surfear-push-sse/SSE%E6%8E%A8%E9%80%81-%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.md)         | ✅ 已实现 |
| app:xxx                   | app推送           | 🚧 开发中 |

## 贡献指南

欢迎贡献代码，请通过以下方式参与：

1. Fork本仓库
2. 创建特性分支
3. 提交更改
4. 创建Pull Request

## 许可证

本项目采用Apache 2.0许可证