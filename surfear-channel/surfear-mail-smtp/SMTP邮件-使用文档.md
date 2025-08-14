## SMTP邮件-使用文档

----------

### 官方文档

[Oracle 官方 JavaMail 文档](https://javaee.github.io/javamail/)


----------

### 功能支持

----------

| 消息/功能类型    | 是否支持 | 模板支持 | 模板支持度        | 特色功能           |
|------------|------|------|--------------|----------------|
| 发送文本邮件     | 是    | 是    | 任意，推荐txt格式   | 模板变量支持         |
| 发送HTML邮件   | 是  | 是    | 任意，推荐html格式     | 模板变量支持         |
| 发送附件邮件     | 是  | 是    | 任意，推荐txt、html格式 | 模板变量支持，多类型附件支持 |


### 特色功能

----------

- 数据结构抽象：提供统一的数据结构抽象，简化消息发送逻辑
- 模板支持：支持txt、markdown、json、html多种模板类型，满足不同消息格式需求
- 模板变量支持：支持模板变量语法${var}填充，方便动态消息内容生成。变量未赋值将原样输出。
- 多类型附件支持：本地资源、项目资源、在线资源、动态生成的资源，多种类型支持
----------

### 使用示例

----------
### Maven引用

```xml
<dependency>
    <groupId>io.github.yangdaowan</groupId>
    <artifactId>surfear-mail-smtp</artifactId>
    <version>${surfear.version}</version>
</dependency>
```

----------

### 配置文件
在 `resources/surfear.yml` 中添加以下配置：
```yaml
surfear:
  # SMTP邮件
  smtp-mail:
    host: "smtp.qq.com"
    user: "xxxxxx@qq.com"
    password: "xxxxxxxxxx"
```
配置项参考
```java
io.github.yangdaowan.surfear.mail.smtp.framework.SmtpMailConfig
```

- 支持多环境配置：
  - 临时配置 > 环境变量 > 项目配置 > 默认配置
- 支持多项目配置文件：
  - `resources/surfear.properties` 及 `resources/surfear.yml`
  - `yml文件`后读取，会覆盖`properties文件`
- 使用参考：顺风耳工具包-配置说明文档.md
----------

### 通道KEY
```java
"mail:smtp"
```

----------

### 使用示例

----------

**1. 发送文本邮件**
```java
SmtpMailModel model = new SmtpMailModel();
model.setTo("xxx@qq.com");
model.setSubject("测试邮件-纯文本");
model.setContent("这是一封由 surfear 发送的，纯文本邮件");

MessageSender.send("mail:smtp", model);
```
----------

**2. 发送HTML邮件**
```java
SmtpMailModel model = new SmtpMailModel();
model.setTo("xxx@qq.com");
model.setSubject("测试邮件-HTML模板");
// 设置HTML内容，不添加模板填充就发送该内容
model.setContent("这是一封由 surfear 发送的，欢迎模版邮件");

// 设置模板填充会覆盖content内容
Map<String, Object> params = new HashMap<>();
params.put("logoUrl", "https://img0.baidu.com/it/u=3846077304,3667902286&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1422");
params.put("appName","顺风耳俱乐部");
params.put("username","某某用户");
params.put("activationUrl","www.baidu.com");
params.put("email","example@mail.surfear.com");
params.put("registrationDate", new Date());
params.put("tosUrl","www.baidu.com");
params.put("privacyUrl","www.baidu.com");
params.put("unsubscribeUrl","www.baidu.com");
model.setTemplateFileName("welcome.html");
model.setTemplateParams(params);

MessageSender.send("mail:smtp", model);
```
----------

**3. 发送附件邮件**
```java
SmtpMailModel model = new SmtpMailModel();
model.setTo("1114389297@qq.com");
model.setSubject("测试邮件-携带附件");
model.setContent("这是一封由 surfear 发送的，携带附件的邮件");

List<MailAttachment> attachments = new ArrayList<>();

// 本地资源
attachments.add(AttachmentFactory.fromFileSystem("C:\\Users\\51293\\Downloads\\顺风耳工具包测试文档.txt"));
// 项目资源
attachments.add(AttachmentFactory.fromResource("templates/mail/welcome.html", "welcome.html"));
// 在线资源
attachments.add(AttachmentFactory.fromUrl("https://img0.baidu.com/it/u=3846077304,3667902286&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1422", "测试附件1.JPEG"));
// 动态生成的资源
byte[] csvData = "Name,Email,Phone\nTest,test@surfear.com,123456789".getBytes();
attachments.add(AttachmentFactory.fromBytes(csvData, "export.csv", "text/csv"));

model.setAttachments(attachments);
MessageSender.send("mail:smtp", model);
```

----------

**响应结果**
```json
{
  "code": "0",
  "data": {
    "messageId": "54e9022b-1df3-48ba-8adc-6b1f27d6df2d"
  },
  "message": "ok",
  "success": true
}
```

----------
**模板管理**


在 `resources/templates/mail` 目录下添加模板文件：`welcome.html`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>欢迎加入我们</title>
  <style>
    body {
      font-family: 'Arial', sans-serif;
      line-height: 1.6;
      color: #333333;
      max-width: 600px;
      margin: 0 auto;
      padding: 20px;
    }
    li {
      list-style: none;
    }
    .header {
      text-align: center;
      padding: 20px 0;
      border-bottom: 1px solid #eeeeee;
    }
    .logo {
      max-width: 150px;
      height: auto;
    }
    .content {
      text-align: center;
      padding: 20px 0;
    }
    .button {
      display: inline-block;
      padding: 10px 20px;
      background-color: #4CAF50;
      color: white;
      text-decoration: none;
      border-radius: 4px;
      margin: 20px 0;
    }
    .footer {
      text-align: center;
      padding: 20px 0;
      border-top: 1px solid #eeeeee;
      font-size: 12px;
      color: #777777;
    }
    @media screen and (max-width: 600px) {
      body {
        padding: 10px;
      }
    }
  </style>
</head>
<body>
<div class="header">
  <img th:src="@{${logoUrl}}" alt="公司Logo" class="logo" th:if="${logoUrl}">
  <h1>欢迎加入<span th:text="${appName}">我们的社区</span>!</h1>
</div>

<div class="content">
  <p>尊敬的<span th:text="${username}">用户</span>,</p>

  <p>感谢您注册<span th:text="${appName}">我们的服务</span>。我们很高兴您能成为我们的一员！</p>

  <p th:if="${activationUrl}">
    请点击下面的按钮激活您的账户：
    <br><br>
    <a th:href="@{${activationUrl}}" class="button">激活账户</a>
  </p>

  <p>您的注册信息：</p>
  <ul>
    <li>用户名: <span th:text="${username}">用户名</span></li>
    <li th:if="${email}">电子邮箱: <span th:text="${email}">email@example.com</span></li>
    <li th:if="${registrationDate}">注册日期: <span th:text="${#dates.format(registrationDate, 'yyyy-MM-dd HH:mm')}">2023-01-01</span></li>
  </ul>

  <p>如果您有任何问题，请随时联系我们的客服团队。</p>

  <p>祝您使用愉快！</p>

  <p>此致<br><span th:text="${appName}">我们的团队</span></p>
</div>

<div class="footer">
  <p>© <span th:text="${#dates.year(#dates.createNow())}">2023</span> <span th:text="${appName}">我们的公司</span>. 保留所有权利.</p>
  <p>
    <a th:href="@{${tosUrl}}" th:if="${tosUrl}">服务条款</a> |
    <a th:href="@{${privacyUrl}}" th:if="${privacyUrl}">隐私政策</a>
  </p>
  <p th:if="${unsubscribeUrl}">
    如果您不希望再收到我们的邮件，请<a th:href="@{${unsubscribeUrl}}">退订</a>。
  </p>
</div>
</body>
</html>
```
注意当前通道为`MAIL`类型，所以模板文件需要放在`resources/templates/mail`目录下。

----------