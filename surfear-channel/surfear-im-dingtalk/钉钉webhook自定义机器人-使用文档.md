## 企微webhook自定义机器人-使用文档

----------

### 官方文档

[钉钉官方文档-自定义机器人发送群消息](https://open.dingtalk.com/document/orgapp/custom-robots-send-group-messages)

----------

### 功能支持

----------

| 消息/功能类型          | 是否支持 | 模板支持 | 模板支持度             | 特色功能     |
|------------------|------|------|-------------------|----------|
| 安全设置-签名校验        | 是    | -    | -                 | 自动签名     |
| Text文本类型           | 是    | 是    | 推荐txt格式，仅支持文本内容部分 | 模板变量支持   |
| Link链接消息          | 是    | 是    | 支持json格式的对象数据     | 模板变量支持   |
| Markdown 类型            | 是    | 是    | 支持md格式            | 模板变量支持   |
| ActionCard 类型             | 是    | 是    | 支持md格式            | 模板变量支持   |
| FeedCard 类型     | 是    | 是    | 支持json格式的数组数据     | 模板变量支持   |
| 上传文件             | 是    | -    | -                 | 简易封装（需应用凭证）           |

### 特色功能

----------

- 数据结构抽象：提供统一的数据结构抽象，简化消息发送逻辑
- 模板支持：支持txt、markdown、json、html多种模板类型，满足不同消息格式需求
- 模板变量支持：支持模板变量语法${var}填充，方便动态消息内容生成。变量未赋值将原样输出。
- 应用Token自动获取：支持应用凭证自动获取，自动缓存，简化请求逻辑
- 接口简易封装：两行代码调用，轻松获取结果
----------

### 使用示例

----------
### Maven引用

```xml
<dependency>
    <groupId>io.github.yangdaowan</groupId>
    <artifactId>surfear-im-dingtalk</artifactId>
    <version>${surfear.version}</version>
</dependency>
```

----------

### 配置文件
在 `resources/surfear.yml` 中添加以下配置：
```yaml
surfear:
  # 钉钉
  dingtalk:
    # 应用凭证
    clientId: "dingstkqhpv8xxxxxx"
    clientSecret: "jaVS3yEVhxrxlRVZHPYrOtFolh0VVILuumVFu7sNDRxxxxxxx"
    webhook-robot:
      # 自定义机器人调用接口的凭证，详见官方文档。
      # 假设webhook是：https://oapi.dingtalk.com/robot/send?access_token=f27f3eaa29ad67073ec04b5fd56c2xxxxxxxxxxxxxxxx
      accessToken: "f27f3eaa29ad67073ec04b5fd56c2xxxxxxxxxxxxxxxx"
      # 签名秘钥，添加自定义机器人时所生成的密匙(可选)，用于请求的签名认证。填写自动签名
      secretKey: "SEC3ff9b1aa8f1224fbd12c1c2425475dxxxxxxxxxxxxxxxx"
```
配置项参考

```java
import io.github.yangdaowan.surfear.im.dingtalk.framework.config.DingtalkConfig;
import io.github.yangdaowan.surfear.im.dingtalk.framework.config.DingtalkWebhookRobotConfig;
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
"im:dingtalk:webhook:robot"
```

----------

### 使用示例

----------
**1. Text文本类型**
```java
DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
        new TextMsg(
                new Text("文本消息测试"),
                // At可为null或不传入，表示不@任何人
                new At()
                        // 表示@所有人
                        .setAtAll()
                        // 表示@指定人，通过手机号或userId
                        .setAtMobiles(Arrays.asList("手机号1", "手机号2"))
                        .setAtUserIds(Arrays.asList("userId1", "userId2"))
        )
);

String channelKey = "im:dingtalk:webhook:robot";
MessageSender.send(channelKey, model);
```
**1.1 发送文本消息-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Dingtalk_文本推送测试.txt`

```text
您正在使用阿里云短信测试服务，体验验证码是：${code}，如非本人操作，请忽略本短信！
```
注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。

```java
DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
        new TextMsg(
            // At可为null或不传入，表示不@任何人
                new At()
                        // 表示@所有人
                        .setAtAll()
                        // 表示@指定人，通过手机号或userId
                        .setAtMobiles(Arrays.asList("手机号1", "手机号2"))
                        .setAtUserIds(Arrays.asList("userId1", "userId2"))
        )
);

model.setTemplateFileName("Dingtalk_文本推送测试.txt");
Map<String, Object> params = new HashMap<>();
params.put("code", "123456");
model.setTemplateParams(params);

String channelKey = "im:dingtalk:webhook:robot";
MessageSender.send(channelKey, model);
```
----------
**2. Link链接消息**
```java
DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
        new LinkMsg(
                new Link(
                    "标题",
                    "文本",
                    "https://img2.baidu.com/it/u=3695592007,709182901&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=450",
                    "https://www.baidu.com"
                ))
);

String channelKey = "im:dingtalk:webhook:robot";
MessageSender.send(channelKey, model);
```
**2.1 Link链接消息-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Dingtalk_Link推送测试模板.json`

```json
{
  "text": "这个即将发布的新版本，创始人xx称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林",
  "title": "时代的火车向前开",
  "picUrl": "${picUrl}",
  "messageUrl": "https://www.dingtalk.com/s?__biz=MzA4NjMwMTA2Ng==&mid=2650316842&idx=1&sn=60da3ea2b29f1dcc43a7c8e4a7c97a16&scene=2&srcid=09189AnRJEdIiWVaKltFzNTw&from=timeline&isappinstalled=0&key=&ascene=2&uin=&devicetype=android-23&version=26031933&nettype=WIFI"
}
```
注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。

```java
DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
        new LinkMsg()
);

model.setTemplateFileName("Dingtalk_Link推送测试模板.json");
Map<String, Object> params = new HashMap<>();
params.put("picUrl", "https://img2.baidu.com/it/u=3695592007,709182901&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=450");
model.setTemplateParams(params);
        
String channelKey = "im:dingtalk:webhook:robot";
MessageSender.send(channelKey, model);
```
----------
**3. Markdown 类型**
```java
DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
        new MarkdownMsg(
                new Markdown("MarkdownMsg标题", "引用 /n> A man who stands for nothing will fall for anything."),
                // At可为null或不传入，表示不@任何人
                new At()
                        // 表示@所有人
                        .setAtAll()
                        // 表示@指定人，通过手机号或userId
                        .setAtMobiles(Arrays.asList("手机号1", "手机号2"))
                        .setAtUserIds(Arrays.asList("userId1", "userId2"))
        )
);

String channelKey = "im:dingtalk:webhook:robot";
MessageSender.send(channelKey, model);
```
**3.1 Markdown-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Dingtalk_Markdown推送测试模板.md`

```markdown
变量是：${code}
#### 杭州天气 @150XXXXXXXX
> 9度，西北风1级，空气良89，相对温度73%
> ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)
> ###### 10点20分发布 [天气](https://www.dingtalk.com)

```
注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。
```java
// 方式一：MarkdownMsg 手动设置标题，模板获取内容
DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
        new MarkdownMsg(
                new Markdown("MarkdownMsg 手动设置标题，模板获取内容"),
                // At可为null或不传入，表示不@任何人
                new At()
                        // 表示@所有人
                        .setAtAll()
                        // 表示@指定人，通过手机号或userId
                        .setAtMobiles(Arrays.asList("手机号1", "手机号2"))
                        .setAtUserIds(Arrays.asList("userId1", "userId2"))
        )
);


model.setTemplateFileName("Dingtalk_Markdown推送测试模板.md");
Map<String, Object> params = new HashMap<>();
params.put("code", "123456");
model.setTemplateParams(params);
        
String channelKey = "im:dingtalk:webhook:robot";
MessageSender.send(channelKey, model);
```
```java
// 方式二：MarkdownMsg 变量设置标题，模板获取内容
DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
        new MarkdownMsg(
                // At可为null或不传入，表示不@任何人
                new At()
                        // 表示@所有人
                        .setAtAll()
                        // 表示@指定人，通过手机号或userId
                        .setAtMobiles(Arrays.asList("手机号1", "手机号2"))
                        .setAtUserIds(Arrays.asList("userId1", "userId2"))
        )
);

model.setTemplateFileName("Dingtalk_Markdown推送测试模板.md");
Map<String, Object> params = new HashMap<>();
params.put("markdown_title", "MarkdownMsg 变量设置标题");
params.put("code", "123456");
model.setTemplateParams(params);
        
String channelKey = "im:dingtalk:webhook:robot";
MessageSender.send(channelKey, model);
```

----------
**4. ActionCard 类型**
```java
DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
        new ActionCardMsg(new ActionCard(
                "ActionCard 消息标题",
                "ActionCard 消息内容，支持markdown格式",
                // 整体跳转按钮标题
                "整体跳转按钮标题",
                // 整体跳转链接
                "https://www.baidu.com",
                // 独立跳转链接，传递按钮组会覆盖整体跳转链接
                Arrays.asList(
                        new Btns("qq按钮", "https://www.qq.com"),
                        new Btns("sina按钮", "https://www.sina.com")
                ),
                // 按钮竖直排列
                "0" 
        ))
);

String channelKey = "im:dingtalk:webhook:robot";
MessageSender.send(channelKey, model);
```

**4.1 ActionCard 类型-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Dingtalk_ActionCard推送测试模板.md`

```markdown
![变量图片](${picUrl})

#### 乔布斯 20 年前想打造的苹果咖啡厅

Apple Store 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划

```
注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。
```java
DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
        new ActionCardMsg(new ActionCard(
                "ActionCard 消息标题",
                // 整体跳转按钮标题
                "整体跳转按钮标题",
                // 整体跳转链接
                "https://www.baidu.com",
                // 独立跳转链接，传递按钮组会覆盖整体跳转链接
                Arrays.asList(
                        new Btns("qq按钮", "https://www.qq.com"),
                        new Btns("sina按钮", "https://www.sina.com")
                ),
                // 按钮竖直排列
                "0"
        ))
);

model.setTemplateFileName("Dingtalk_ActionCard推送测试模板.md");
Map<String, Object> params = new HashMap<>();
params.put("picUrl", "https://img2.baidu.com/it/u=3695592007,709182901&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=450");
model.setTemplateParams(params);
        
String channelKey = "im:dingtalk:webhook:robot";
MessageSender.send(channelKey, model);
```

----------
**5. FeedCard 类型**
```java
List<Links> links = new ArrayList<>();
for (int i = 0; i < 5; i++) {
        links.add(new Links(
                    "标题" + i,
                    "https://img2.baidu.com/it/u=3695592007,709182901&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=450",
                    "https://www.baidu.com"
        ));
}

DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
        new FeedCardMsg(new FeedCard(links))
);

String channelKey = "im:dingtalk:webhook:robot";
MessageSender.send(channelKey, model);
```

**5.1 FeedCard 类型-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Dingtalk_FeedCard推送测试模板.json`

```json
[
  {
    "title": "时代的火车向前开1",
    "messageURL": "https://www.dingtalk.com/",
    "picURL": "${picUrl}"
  },
  {
    "title": "时代的火车向前开2",
    "messageURL": "https://www.dingtalk.com/",
    "picURL": "https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png"
  }
]
```
注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。
```java
DingtalkWebhookRobotModel model = new DingtalkWebhookRobotModel(
        new FeedCardMsg()
);

model.setTemplateFileName("Dingtalk_FeedCard推送测试模板.json");
Map<String, Object> params = new HashMap<>();
params.put("picUrl", "https://img2.baidu.com/it/u=3695592007,709182901&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=450");
model.setTemplateParams(params);
        
String channelKey = "im:dingtalk:webhook:robot";
MessageSender.send(channelKey, model);
```

----------
**6. 文件上传接口**
```java
File file = new File("C:\\Users\\51293\\Downloads\\测试图片.png");

// 方式一：手动设置配置
DingtalkWebhookRobotConfig config = new DingtalkWebhookRobotConfig();
config.setClientId("应用ID");
config.setClientSecret("应用秘钥");
config.setAccessToken("机器人接口的凭证");
config.setSecretKey("签名秘钥");
// 方式二：从项目中获取配置
// DingtalkWebhookRobotConfig config = ConfigUtil.getChannelConfigObject(null, DingtalkWebhookRobotConfig.class);

DingtalkWebhookUploadMediaApi uploadMediaApi = new DingtalkWebhookUploadMediaApi(config, "image", file);
JSONObject response = uploadMediaApi.parseResponse(new OkhttpClient().execute(uploadMediaApi));

System.out.println(response);

```