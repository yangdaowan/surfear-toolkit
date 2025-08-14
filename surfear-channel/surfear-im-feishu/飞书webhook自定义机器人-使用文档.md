## 企微webhook自定义机器人-使用文档

----------

### 官方文档

[飞书官方文档-自定义机器人使用指南](https://open.feishu.cn/document/client-docs/bot-v3/add-custom-bot#478cb64f)

----------

### 功能支持

----------

| 消息/功能类型          | 是否支持 | 模板支持 | 模板支持度             | 特色功能        |
|------------------|------|------|-------------------|-------------|
| 安全设置-签名校验        | 是    | -    | -                 | 自动签名        |
| 发送文本消息           | 是    | 是    | 推荐txt格式，仅支持文本内容部分 | 模板变量支持      |
| 发送富文本消息          | 是    | 是    | 支持json格式的对象数据     | 模板变量支持      |
| 发送群名片            | -    | -    | -                 | -           |
| 发送图片             | 是   | -    | -                 | 自动上传（需应用凭证） |
| 发送飞书卡片-自定义卡片     | 是    | 是    | 支持json格式的对象数据     | 模板变量支持      |
| 发送飞书卡片-模板卡片      | 是    | -    | -                 | -           |
| 上传图片             | 是    | -    | -                 | 简易封装（需应用凭证）           |
| 上传文件             | 是    | -    | -                 | 简易封装（需应用凭证）           |

### 特色功能

----------

- 数据结构抽象：提供统一的数据结构抽象，简化消息发送逻辑
- 模板支持：支持txt、markdown、json、html多种模板类型，满足不同消息格式需求
- 模板变量支持：支持模板变量语法${var}填充，方便动态消息内容生成。变量未赋值将原样输出。
- 图片自动上传：支持图片类型的文件内容自动上传，减少取数据操作
- 应用Token自动获取：支持应用凭证自动获取，自动缓存，简化请求逻辑
- 接口简易封装：两行代码调用，轻松获取结果
----------

### 使用示例

----------
### Maven引用

```xml
<dependency>
    <groupId>io.github.yangdaowan</groupId>
    <artifactId>surfear-im-feishu</artifactId>
    <version>${surfear.version}</version>
</dependency>
```

----------

### 配置文件
在 `resources/surfear.yml` 中添加以下配置：
```yaml
surfear:
  # 飞书
  feishu:
    # 应用凭证，开发者后台获取。可选，如果不配置则不支持使用飞书图片或文件上传接口，不影响机器人功能
    appId: "cli_xxxxxxxxxxxxxx"
    appSecret: "xxxxxxxxxxxxxx"
    webhook-robot:
      # 机器人key，详见官方文档。
      # 假设webhook是：https://open.feishu.cn/open-apis/bot/v2/hook/5a128de8-753d-xxxxxxxxxxxxxx
      accessToken: "5a128de8-753d-xxxxxxxxxxxxxx"
      # 签名秘钥，添加自定义机器人时所生成的密匙(可选)，用于请求的签名认证。填写自动签名
      secretKey: "82NuEe2xxxxxxxxxxxxxx"
```
配置项参考
```java
io.github.yangdaowan.surfear.im.feishu.framework.config.FeishuConfig
io.github.yangdaowan.surfear.im.feishu.framework.config.FeishuWebhookRobotConfig
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
"im:feishu:webhook:robot"
```

----------

### 使用示例

----------
**1. 发送文本消息**
```java
FeishuWebhookRobotModel model = new FeishuWebhookRobotModel(
        new TextMsg(
                new Text("测试消息推送-发送文本消息"),
                // At可为null或不传入，表示不@任何人
                new At()
                        // 表示@所有人
                        .setAtAll()
                        // 表示@指定人
                        .setAtOpenId("openId")
        )
);

String channelKey = "im:feishu:webhook:robot";
MessageSender.send(channelKey, model);
```
**1.1 发送文本消息-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Feishu_文本模板.txt`

```text
Feishu_文本模板.txt，存在变量：${a}、${b}、${c}
```
注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。

```java
FeishuWebhookRobotModel model = new FeishuWebhookRobotModel(
        new TextMsg(
                // At可为null或不传入，表示不@任何人
                new At()
                        // 表示@所有人
                        .setAtAll()
                        // 表示@指定人
                        .setAtOpenId("openId")
        )
);

model.setTemplateFileName("Feishu_文本消息测试.txt");

Map<String, Object> params = new HashMap<>();
params.put("a", "顺风耳");
params.put("b", "工具包");
params.put("c", 1.0);
model.setTemplateParams(params);

String channelKey = "im:feishu:webhook:robot";
MessageSender.send(channelKey, model);
```
----------
**2. 发送富文本消息**
```java
// 构造富文本内容体
List<List<RichText.ParagraphNode>> contents = new ArrayList<>();

// 单个段落内容
RichText.Paragraph paragraph = new RichText.Paragraph()
  .addParagraphNode(
          new RichText.ParagraphNode.Text("这是一个文本内容") // 添加文本内容
  )
  .addParagraphNode(
          new RichText.ParagraphNode.A("这是一个超链接", "https://www.baidu.com/") // 添加超链接
  )
  .addParagraphNode(
          new RichText.ParagraphNode.Img("img_ecffc3b9-8f14-400f-a014-05eca1a4310g") // 添加图片
  )
  .addParagraphNode(
          new RichText.ParagraphNode.At() // @所有人
//        new RichText.ParagraphNode.At("openId") // @指定人
  );
contents.add(paragraph.getParagraphNodes());

FeishuWebhookRobotModel model = new FeishuWebhookRobotModel(
  new RichTextMsg(
          new RichText("测试富文本推送-标题内容", contents)
  )
);

String channelKey = "im:feishu:webhook:robot";
MessageSender.send(channelKey, model);
```
**2.1 发送富文本消息-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Feishu_富文本消息模板.json`

```json
{
  "title": "${title}项目更新通知",
  "content": [
    [{
      "tag": "text",
      "text": "项目有更新: "
    }, {
      "tag": "a",
      "text": "请查看",
      "href": "http://www.example.com/"
    }, {
      "tag": "at",
      "user_id": "openId"
    }]
  ]
}
```
注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。

```java
FeishuWebhookRobotModel model = new FeishuWebhookRobotModel(
        new RichTextMsg()
);

model.setTemplateFileName("Feishu_富文本消息模板.json");

Map<String, Object> params = new HashMap<>();
params.put("title", "顺风耳");
model.setTemplateParams(params);
```
----------
**3. 发送图片**
```java
FeishuWebhookRobotModel model = new FeishuWebhookRobotModel(
  new ImageMsg(
          new Image("img_ecffc3b9-8f14-400f-a014-05eca1a4310g")
  )
);

String channelKey = "im:feishu:webhook:robot";
MessageSender.send(channelKey, model);
```
**3.1 发送图片-特色功能**
```java
// 特色功能-自动上传
FeishuWebhookRobotModel model = new FeishuWebhookRobotModel(
        new AutoImageMsg(new File("C:\\Users\\51293\\Downloads\\测试图片.png"))
);

String channelKey = "im:feishu:webhook:robot";
MessageSender.send(channelKey, model);
```

----------
**4. 发送飞书卡片-自定义卡片-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Feishu_卡片消息模板_自定义卡片.json`

```json
{
  "schema": "2.0",
  "config": {
    "update_multi": true,
    "style": {
      "text_size": {
        "normal_v2": {
          "default": "normal",
          "pc": "normal",
          "mobile": "heading"
        }
      }
    }
  },
  "body": {
    "direction": "vertical",
    "padding": "12px 12px 12px 12px",
    "elements": [
      {
        "tag": "markdown",
        "content": "西湖，位于中国浙江省杭州市西湖区龙井路1号，杭州市区西部，汇水面积为21.22平方千米，湖面面积为6.38平方千米。",
        "text_align": "left",
        "text_size": "normal_v2",
        "margin": "0px 0px 0px 0px"
      },
      {
        "tag": "button",
        "text": {
          "tag": "plain_text",
          "content": "🌞更多景点介绍"
        },
        "type": "default",
        "width": "default",
        "size": "medium",
        "behaviors": [
          {
            "type": "open_url",
            "default_url": "https://baike.baidu.com/item/%E8%A5%BF%E6%B9%96/4668821",
            "pc_url": "",
            "ios_url": "",
            "android_url": ""
          }
        ],
        "margin": "0px 0px 0px 0px"
      }
    ]
  },
  "header": {
    "title": {
      "tag": "plain_text",
      "content": "今日工具推荐：${title}"
    },
    "subtitle": {
      "tag": "plain_text",
      "content": ""
    },
    "template": "blue",
    "padding": "12px 12px 12px 12px"
  }
}
```
注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。
```java
FeishuWebhookRobotModel model = new FeishuWebhookRobotModel(
    new CustomCardMsg()
);

model.setTemplateFileName("Feishu_卡片消息模板_自定义卡片.json");
Map<String, Object> params = new HashMap<>();
params.put("title", "顺风耳");
model.setTemplateParams(params);

String channelKey = "im:feishu:webhook:robot";
MessageSender.send(channelKey, model);
```
**4.1 发送飞书卡片-模板卡片**


[飞书官方文档-使用搭建工具创建并发布卡片](https://open.feishu.cn/document/feishu-cards/quick-start/send-message-cards-with-custom-bot#bad5a929)
```markdown
- 选择参考案例库
- 选择‘办公设备申请（领取成功）’案例
- 添加自定义变量
- 右上角点击‘添加自定义机器人’按钮
- 左上角复制卡片ID
- 点击‘发布’按钮
```
```java
Map<String, Object> params = new HashMap<>();
params.put("template_version_name", "1.0.0");
params.put("desc", "这是顺风耳工具包测试消息");
params.put("num", "99");
params.put("addr", "北京市天安门广场");
params.put("no", "测试单号00000999");
params.put("url", "www.baidu.com");

FeishuWebhookRobotModel model = new FeishuWebhookRobotModel(
      new TemplateCardMsg(
              new TemplateCard(
                      new TemplateCardData(
                              "AAqzrqdMRIBn1", // 飞书官方模板卡片ID
                              params // 飞书官方模板变量参数
                      )
              )
      )
);

String channelKey = "im:feishu:webhook:robot";
MessageSender.send(channelKey, model);
```
`template_version_name`参数在模板参数中定义，表示卡片模板版本名称，默认不填为`1.0.0`。会自动填充到官方接口参数中。

----------
**9. 文件上传接口**
```java
File file = new File("C:\\Users\\51293\\Downloads\\测试图片.png");

// 方式一：手动设置配置
// FeishuWebhookRobotConfig config = new FeishuWebhookRobotConfig();
// config.setAppId("应用ID");
// config.setAppSecret("应用秘钥");
// config.setAccessToken("配置key");
// config.setSecretKey("签名秘钥");
// 方式二：从项目中获取配置
FeishuWebhookRobotConfig config = ConfigUtil.getChannelConfigObject(null, FeishuWebhookRobotConfig.class);

FeishuWebhookUploadMediaApi uploadMediaApi = new FeishuWebhookUploadMediaApi(config, "images", file);
JSONObject response = uploadMediaApi.parseResponse(new OkhttpClient().execute(uploadMediaApi));

System.out.println(response);
```