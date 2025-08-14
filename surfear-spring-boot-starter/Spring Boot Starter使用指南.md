# Surfear Spring Boot Starter 使用指南

## 📋 项目结构要求

```
your-spring-boot-project/
├── src/main/
│   ├── java/
│   │   └── com/example/demo/
│   │       ├── DemoApplication.java
│   │       └── controller/
│   │           └── MessageController.java
│   └── resources/
│       ├── application.yml          # Spring Boot配置
│       └── surfear.yaml            # Surfear配置（必需）
├── pom.xml
└── README.md
```

## 🚀 完整示例

### 1. Maven依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>demo</name>
    <description>Surfear Spring Boot Demo</description>
    
    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Surfear Spring Boot Starter -->
        <dependency>
            <groupId>io.github.yangdaowan</groupId>
            <artifactId>surfear-spring-boot-starter</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
</project>
```

### 2. 配置文件

#### application.yml
```yaml
server:
   port: 8080

spring:
   application:
      name: surfear-spring-boot-demo

logging:
   level:
      io.github.yangdaowan.surfear: INFO
      com.example.demo: DEBUG
   pattern:
      console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
      file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

#### surfear.yaml
```yaml
surfear:
  # 钉钉
  dingtalk:
    clientId: "xxxx"
    clientSecret: "xxxx"
    webhook-robot:
      accessToken: "xxxx"
      secretKey: "xxxx"
  # 飞书
  feishu:
    appId: "xxxx"
    appSecret: "xxxx"
    webhook-robot:
      accessToken: "xxxx"
      secretKey: "xxxx"
  # 企业微信
  wecom:
    webhook-robot:
      accessToken: "xxxx"
  # 阿里云
  aliyun:
    accessKey: "xxxx"
    secretKey: "xxxx"
  # 百度云
  baidu:
    accessKey: "xxxx"
    secretKey: "xxxx"
  # 腾讯云
  tencent:
    secretId: "xxxx"
    secretKey: "xxxx"
  # SMTP邮件
  smtp-mail:
    host: "smtp.xxxx.com"
    user: "xxxx@qq.com"
    password: "xxxx"
```

### 3. 启动类

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 4. 控制器示例

```java
package com.example.demo.controller;

import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import io.github.yangdaowan.surfear.core.util.MessageSender;
import io.github.yangdaowan.surfear.mail.smtp.framework.SmtpMailModel;
import io.github.yangdaowan.surfear.sms.aliyun.framework.AliyunSmsModel;
import io.github.yangdaowan.surfear.im.dingtalk.framework.model.DingtalkWebhookRobotModel;
import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.TextMsg;
import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.custom.Text;
import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.custom.At;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    
    /**
     * 发送邮件
     */
    @PostMapping("/email")
    public MessageResult sendEmail(@RequestBody EmailRequest request) {
        SmtpMailModel model = new SmtpMailModel();
        model.setTo(request.getTo());
        model.setSubject(request.getSubject());
        model.setContent(request.getContent());
        
        return MessageSender.send("mail:smtp", model);
    }
    
    /**
     * 发送短信
     */
    @PostMapping("/sms")
    public MessageResult sendSms(@RequestBody SmsRequest request) {
        AliyunSmsModel model = new AliyunSmsModel();
        model.setPhoneNumbers(request.getPhone());
        model.setSignName(request.getSignName());
        model.setTemplateCode(request.getTemplateCode());
        model.setTemplateParam(request.getParams());
        
        return MessageSender.send("sms:aliyun", model);
    }
    
    /**
     * 发送钉钉消息
     */
    @PostMapping("/dingtalk")
    public MessageResult sendDingTalk(@RequestBody DingTalkRequest request) {
        DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
            new TextMsg(
                new Text(request.getContent())
            )
        );
        
        return MessageSender.send("im:dingtalk:webhook:robot", model);
    }
    
    /**
     * 异步发送邮件
     */
    @PostMapping("/email/async")
    public CompletableFuture<MessageResult> sendEmailAsync(@RequestBody EmailRequest request) {
        SmtpMailModel model = new SmtpMailModel();
        model.setTo(request.getTo());
        model.setSubject(request.getSubject());
        model.setContent(request.getContent());
        
        return MessageSender.sendAsync("mail:smtp", model);
    }
    
    // 请求对象
    @Data
    public static class EmailRequest {
        private String to;
        private String subject;
        private String content;
    }
    
    @Data
    public static class SmsRequest {
        private String phone;
        private String signName;
        private String templateCode;
        private Map<String, Object> params;
    }
    
    @Data
    public static class DingTalkRequest {
        private String content;
    }
}
```

### 5. 服务层示例

```java
package com.example.demo.service;

import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import io.github.yangdaowan.surfear.core.util.MessageSender;
import io.github.yangdaowan.surfear.mail.smtp.framework.SmtpMailModel;
import io.github.yangdaowan.surfear.sms.aliyun.framework.AliyunSmsModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class NotificationService {
    
    /**
     * 发送欢迎邮件
     */
    public void sendWelcomeEmail(String email, String userName) {
        SmtpMailModel model = new SmtpMailModel();
        model.setTo(email);
        model.setSubject("欢迎注册我们的服务");
        
        // 使用模板
        model.setTemplateFileName("welcome.html");
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        params.put("loginUrl", "https://example.com/login");
        model.setTemplateParams(params);
        
        MessageResult result = MessageSender.send("mail:smtp", model);
        if (result.isSuccess()) {
            log.info("欢迎邮件发送成功: {}", email);
        } else {
            log.error("欢迎邮件发送失败: {}", result.getError().getMessage());
        }
    }
    
    /**
     * 发送验证码短信
     */
    public void sendVerificationSms(String phone, String code) {
        AliyunSmsModel model = new AliyunSmsModel();
        model.setPhoneNumbers(phone);
        model.setSignName("您的应用");
        model.setTemplateCode("SMS_123456789");
        
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        model.setTemplateParam(params);
        
        MessageResult result = MessageSender.send("sms:aliyun", model);
        if (result.isSuccess()) {
            log.info("验证码短信发送成功: {}", phone);
        } else {
            log.error("验证码短信发送失败: {}", result.getError().getMessage());
        }
    }
    
    /**
     * 批量发送通知
     */
    public void sendBatchNotifications(List<String> emails, String subject, String content) {
        List<CompletableFuture<MessageResult>> futures = emails.stream()
            .map(email -> {
                SmtpMailModel model = new SmtpMailModel();
                model.setTo(email);
                model.setSubject(subject);
                model.setContent(content);
                return MessageSender.sendAsync("mail:smtp", model);
            })
            .collect(Collectors.toList());
        
        // 等待所有邮件发送完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .whenComplete((result, throwable) -> {
                if (throwable == null) {
                    log.info("批量邮件发送完成，共 {} 封", emails.size());
                } else {
                    log.error("批量邮件发送失败", throwable);
                }
            });
    }
}
```

## 🔧 高级配置示例

### 1. SSE推送服务配置

```java
package com.example.demo.config;

import io.github.yangdaowan.surfear.push.sse.core.config.SseServerConfig;
import io.github.yangdaowan.surfear.push.sse.core.server.SseNettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SSE (Server-Sent Events) 推送服务配置
 *
 * @author surfear-demo
 * @version 1.0.0
 */
@Configuration
@Slf4j
public class SseConfiguration {

   /**
    * 配置SSE服务器
    * 只有在配置文件中启用SSE时才会创建此Bean
    *
    * @return SSE服务器实例
    */
   @Bean
   @ConditionalOnClass(value =  SseNettyServer.class)
   public SseNettyServer sseServer() {
      log.info("开始配置SSE推送服务...");

      // 创建SSE服务器配置
      SseServerConfig config = SseServerConfig.builder().build();
      config.setPort(8081);  // SSE服务端口，避免与主应用端口冲突
      config.setPath("/sse"); // SSE服务路径
      config.setAllowCors(true); // 允许跨域访问

      // 创建并配置SSE服务器
      SseNettyServer server = SseNettyServer.createInstance(config);

      // 异步启动服务器
      server.start().whenComplete((result, throwable) -> {
         if (throwable == null) {
            log.info("SSE推送服务启动成功！");
            log.info("- 端口: {}", config.getPort());
            log.info("- 路径: {}", config.getPath());
            log.info("- 跨域: {}", config.isAllowCors() ? "允许" : "禁止");
            log.info("- 访问地址: http://localhost:{}{}", config.getPort(), config.getPath());
         } else {
            log.error("SSE推送服务启动失败", throwable);
         }
      });

      // 添加关闭钩子
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
         log.info("正在关闭SSE推送服务...");
         server.stop();
         log.info("SSE推送服务已关闭");
      }));

      return server;
   }
}

```

### 2. 短信回调服务配置

```java
package com.example.demo.config;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.smscallback.SmsCallbackHandler;
import io.github.yangdaowan.surfear.push.sse.core.server.SseNettyServer;
import io.github.yangdaowan.surfear.sms.aliyun.core.api.callback.AliyunSmsCallbackConfig;
import io.github.yangdaowan.surfear.sms.aliyun.core.api.callback.AliyunSmsCallbackServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信回调服务配置
 * 用于接收短信发送状态和用户回复
 *
 * @author surfear-demo
 * @version 1.0.0
 */
@Configuration
@Slf4j
public class SmsCallbackConfiguration {

   /**
    * 配置阿里云短信回调服务器
    * 只有在配置文件中启用短信回调时才会创建此Bean
    *
    * @return 阿里云短信回调服务器实例
    */
   @Bean
   @ConditionalOnClass(value =  AliyunSmsCallbackServer.class)
   public AliyunSmsCallbackServer aliyunCallbackServer() {
      log.info("开始配置阿里云短信回调服务...");

      // 创建回调服务器配置
      AliyunSmsCallbackConfig config = new AliyunSmsCallbackConfig();
      config.setPort(8082); // 回调服务端口，避免与主应用和SSE端口冲突
      config.setPath("/callback/aliyun"); // 回调路径

      // 创建回调处理器
      SmsCallbackHandler<JSONArray> callbackHandler = new SmsCallbackHandler<JSONArray>() {

         @Override
         public void handleStatusReport(JSONArray data) {
            log.info("收到阿里云短信状态回调: {}", data.toJSONString());

            // TODO: 处理短信状态回调逻辑
            // 1. 解析回调数据
            // 2. 更新数据库中的发送状态
            // 3. 触发相应的业务逻辑

            try {
               for (int i = 0; i < data.size(); i++) {
                  JSONObject item = data.getJSONObject(i);
                  String phoneNumber = item.getString("phone_number");
                  String sendDate = item.getString("send_date");
                  String content = item.getString("content");
                  String status = item.getString("status");
                  String errCode = item.getString("err_code");
                  String errMsg = item.getString("err_msg");

                  log.info("短信状态更新 - 手机号: {}, 发送时间: {}, 状态: {}, 错误码: {}, 错误信息: {}",
                          phoneNumber, sendDate, status, errCode, errMsg);

                  // 根据状态执行相应逻辑
                  switch (status) {
                     case "DELIVRD":
                        log.info("短信发送成功: {}", phoneNumber);
                        // 更新为发送成功状态
                        break;
                     case "UNDELIV":
                        log.warn("短信发送失败: {}, 原因: {}", phoneNumber, errMsg);
                        // 更新为发送失败状态，可能需要重试
                        break;
                     default:
                        log.info("短信状态: {} - {}", status, phoneNumber);
                        break;
                  }
               }
            } catch (Exception e) {
               log.error("处理短信状态回调时发生错误", e);
            }
         }

         @Override
         public void handleUplinkMessage(JSONArray data) {
            log.info("收到阿里云短信上行回调: {}", data.toJSONString());

            // TODO: 处理用户回复短信逻辑
            // 1. 解析用户回复内容
            // 2. 根据回复内容触发相应业务逻辑
            // 3. 可能需要自动回复

            try {
               for (int i = 0; i < data.size(); i++) {
                  JSONObject item = data.getJSONObject(i);
                  String phoneNumber = item.getString("phone_number");
                  String content = item.getString("content");
                  String receiveDate = item.getString("receive_date");
                  String destCode = item.getString("dest_code");

                  log.info("收到用户回复 - 手机号: {}, 内容: {}, 接收时间: {}, 目标号码: {}",
                          phoneNumber, content, receiveDate, destCode);

                  // 根据回复内容执行相应逻辑
                  if (content != null) {
                     String reply = content.trim().toUpperCase();
                     switch (reply) {
                        case "TD":
                        case "退订":
                           log.info("用户申请退订: {}", phoneNumber);
                           // 添加到退订列表
                           break;
                        case "复活":
                        case "恢复":
                           log.info("用户申请恢复订阅: {}", phoneNumber);
                           // 从退订列表移除
                           break;
                        default:
                           log.info("收到用户普通回复: {} - {}", phoneNumber, content);
                           // 处理其他回复内容
                           break;
                     }
                  }
               }
            } catch (Exception e) {
               log.error("处理短信上行回调时发生错误", e);
            }
         }

         @Override
         public void handleUnknownCallback(JSONArray data) {
            log.warn("收到未知类型的回调数据: {}", data.toJSONString());
            // 记录未知回调，便于后续分析
         }
      };

      // 创建回调服务器
      AliyunSmsCallbackServer server = AliyunSmsCallbackServer.createInstance(config, callbackHandler);

      // 异步启动服务器
      server.start().whenComplete((result, throwable) -> {
         if (throwable == null) {
            log.info("阿里云短信回调服务启动成功！");
            log.info("- 端口: {}", config.getPort());
            log.info("- 路径: {}", config.getPath());
            log.info("- 回调地址: http://localhost:{}{}", config.getPort(), config.getPath());
            log.info("请在阿里云短信控制台配置此回调地址");
         } else {
            log.error("阿里云短信回调服务启动失败", throwable);
         }
      });

      // 添加关闭钩子
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
         log.info("正在关闭阿里云短信回调服务...");
         server.stop();
         log.info("阿里云短信回调服务已关闭");
      }));

      return server;
   }
}
```

## 🧪 测试示例

### 单元测试

```java
package com.example.demo.service;

import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationServiceTest {
    
    @Autowired
    private NotificationService notificationService;
    
    @Test
    void testSendWelcomeEmail() {
        // 注意：这需要真实的邮件配置
        notificationService.sendWelcomeEmail("test@example.com", "测试用户");
        // 验证邮件发送逻辑
    }
    
    @Test
    void testSendVerificationSms() {
        // 注意：这需要真实的短信配置，且可能产生费用
        notificationService.sendVerificationSms("13800138000", "123456");
        // 验证短信发送逻辑
    }
}
```

## 🔍 故障排查

### 常见问题

1. **找不到配置文件**
   ```
   错误：未找到项目配置文件: surfear.properties 或 surfear.yaml 在任何标准位置
   解决：确保 surfear.yaml 文件在 src/main/resources 目录下
   ```

2. **通道初始化失败**
   ```
   错误：消息通道不存在：mail:smtp
   解决：检查依赖是否正确引入，查看启动日志中的可用通道列表
   ```

3. **配置项错误**
   ```
   错误：邮件发送失败
   解决：检查 surfear.yaml 中的邮件配置是否正确
   ```

### 调试技巧

1. **开启调试日志**
   ```yaml
   logging:
     level:
       io.github.yangdaowan.surfear: DEBUG
   ```

2. **检查可用通道**
   ```java
   @Autowired
   private SurfearChannelInitializer initializer;
   
   public void checkChannels() {
       log.info("可用通道: {}", initializer.getAvailableChannels());
   }
   ```
