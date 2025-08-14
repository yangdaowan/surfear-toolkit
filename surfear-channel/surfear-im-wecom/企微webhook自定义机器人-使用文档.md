## 企微webhook自定义机器人-使用文档

----------

### 官方文档

[企业微信官方文档-群机器人配置说明](https://developer.work.weixin.qq.com/document/path/99110)

----------

### 功能支持

----------

| 消息/功能类型       | 是否支持 | 模板支持 | 模板支持度                       | 特色功能       |
|---------------|-----|------|-----------------------------|------------|
| 文本类型          | 是   | 是    | 推荐txt格式，仅支持文本内容部分 | 模板变量支持     |
| markdown类型    | 是   | 是    | 推荐md格式                      | 模板变量支持     |
| markdown_v2类型 | 是   | 是    | 推荐md格式                      | 模板变量支持     |
| 图片类型          | 是   | -    | -                           | 自动base64转换 |
| 图文类型          | 是   | 是    | 支持json格式的数组数据               | 模板变量支持     |
| 文件类型          | 是   | -    | -                           | 自动上传       |
| 语音类型          | 是   | -    | -                           | 自动上传       |
| 模版卡片类型        | 是   | 是    | 支持json格式的对象数据               | 模板变量支持     |
| 文件上传接口        | 是   | -    | -                           | 接口封装       |

### 特色功能

----------

- 数据结构抽象：提供统一的数据结构抽象，简化消息发送逻辑
- 模板支持：支持txt、markdown、json、html多种模板类型，满足不同消息格式需求
- 模板变量支持：支持模板变量语法${var}填充，方便动态消息内容生成。变量未赋值将原样输出。
- 文件自动上传：支持文件类型、语音类型的文件内容自动上传，减少取数据操作
- 图片自动转换：支持图片类型的base64自动转换，简化图片处理流程
- 接口简易封装：两行代码调用，轻松获取结果
----------

### 使用示例

----------
### Maven引用

```xml
<dependency>
    <groupId>io.github.yangdaowan</groupId>
    <artifactId>surfear-im-wecom</artifactId>
    <version>${surfear.version}</version>
</dependency>
```

----------

### 配置文件
在 `resources/surfear.yml` 中添加以下配置：
```yaml
surfear:
  # 企业微信
  wecom:
    webhook-robot:
      # 机器人key，详见官方文档。
      # 假设webhook是：https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=693a91f6-7xxx-4bc4-xxxxxxxxxxxxxx
      accessToken: "693a91f6-7xxx-4bc4-xxxxxxxxxxxxxx"
```
配置项参考
```java
io.github.yangdaowan.surfear.im.wecom.framework.config.WecomWebhookRobotConfig
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
"im:wecom:webhook:robot"
```

----------

### 使用示例

----------
**1. 文本类型**
```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
        new TextMsg(
                new Text("测试消息推送-文本类型")
        )
);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```
**1.1 文本类型-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Wecom_文本模板.txt`

```text
Wecom_文本模板.txt，存在变量：${a}、${b}、${c}
```
注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。

```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
        new TextMsg(new Text()
                // 表示@所有人
                .setAtAll()
                // 表示@指定人，通过手机号或userId
                .setMentioned_mobile_list(Arrays.asList("手机号1", "手机号2", "@all"))
                .setMentioned_list(Arrays.asList("userId1", "userId2", "@all"))
        )
);

// 无@对象，直接创建空对象
//WecomWebhookRobotModel model = new WecomWebhookRobotModel(
//        new TextMsg()
//);

// 设置模板ID
model.setTemplateFileName("Wecom_文本模板.md");
// 设置模板参数
Map<String, Object> params = new HashMap<>();
params.put("a", "顺风耳");
params.put("b", "工具包");
params.put("c", 1.0);
model.setTemplateParams(params);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```
----------
**2. markdown类型**
```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
        new MarkdownMsg(
               new Markdown(
                       "实时新增用户反馈<font color=\"warning\">132例</font>，请相关同事注意。\n" +
                               ">类型:<font color=\"comment\">用户反馈</font>\n" +
                               ">普通用户反馈:<font color=\"comment\">117例</font>\n" +
                               ">VIP用户反馈:<font color=\"comment\">15例</font>")
        )
);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```
**2.1 markdown类型-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Wecom_markdown类型MD模板.md`

```markdown
Wecom_markdown类型MD模板.md
## 标题变量${title}
[链接名称变量：${linkName}](${linkUrl})
<font color="${color}">${colorName}</font>
```
注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。

```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(new MarkdownMsg());

model.setTemplateFileName("Wecom_markdown类型MD模板.md");
Map<String, Object> params = new HashMap<>();
params.put("title", "顺风耳");
params.put("linkName", "百度");
params.put("linkUrl", "www.baidu.com");
params.put("color", "info");
params.put("colorName", "绿色");
model.setTemplateParams(params);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```
----------
**3. markdown_v2类型**
```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
      new MarkdownV2Msg(
              new MarkdownV2(
                      "# 一、标题\n" +
                              "## 二级标题\n" +
                              "### 三级标题\n" +
                              "# 二、字体\n" +
                              "*斜体*\n" +
                              "\n" +
                              "**加粗**\n" +
                              "# 三、列表 \n" +
                              "- 无序列表 1 \n" +
                              "- 无序列表 2\n" +
                              "  - 无序列表 2.1\n" +
                              "  - 无序列表 2.2\n" +
                              "1. 有序列表 1\n" +
                              "2. 有序列表 2\n" +
                              "# 四、引用\n" +
                              "> 一级引用\n" +
                              ">>二级引用\n" +
                              ">>>三级引用\n" +
                              "# 五、链接\n" +
                              "[这是一个链接](https:work.weixin.qq.com\\/api\\/doc)\n" +
                              "![](https://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png)\n" +
                              "# 六、分割线\n" +
                              "\n" +
                              "---\n" +
                              "# 七、代码\n" +
                              "`这是行内代码`\n" +
                              "```\n" +
                              "这是独立代码块\n" +
                              "```\n" +
                              "\n" +
                              "# 八、表格\n" +
                              "| 姓名 | 文化衫尺寸 | 收货地址 |\n" +
                              "| :----- | :----: | -------: |\n" +
                              "| 张三 | S | 广州 |\n" +
                              "| 李四 | L | 深圳 |\n")
      )
);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```
**3.1 markdown_v2类型-模板填充**

参考 2.1 markdown类型-模板填充

----------
**4. 图片类型**
```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
        new ImageMsg(
                new Image(
                        "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",
                        "5b59b03ea2085d0444cbb52d9d64d6ef"
                )
        )
);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```
**4.1 图片类型-特色功能**
```java
// 特色功能-自动转base64图片
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
                new AutoImageMsg(new File("C:\\Users\\51293\\Downloads\\测试图片.png"))
        );

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```

----------
**5. 图文类型**
```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
        new NewsMsg(
                // 使用对象构造时，单个文章内容可以直接传入
                new News(
                        new Article(
                                "中秋节礼品领取",
                                "今年中秋节公司有豪礼相送",
                                "www.qq.com",
                                "https://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png")
                // 也可以继续使用 addArticle 方法添加更多文章
                ).addArticle(
                        new Article(
                                "图文消息标题",
                                "图文消息描述",
                                "https://example.com/image.jpg",
                                "https://example.com/link"
                        )
                )
        )
);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```
**5.1 图文类型-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Wecom_图文类型模板.json`

```json
[
  {
    "title" : "${title}",
    "description" : "今年中秋节公司有豪礼相送22",
    "url" : "www.qq.com",
    "picurl" : "https://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png"
  }
]
```
注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。

```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
        new NewsMsg());

model.setTemplateFileName("Wecom_图文类型模板.json");
Map<String, Object> params = new HashMap<>();
params.put("title", "顺风耳");
model.setTemplateParams(params);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```

----------
**6. 文件类型**
```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
        new FileMsg(
                new MediaFile("3a8asd892asd8asd")
        )
);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```
**6.1 文件类型-特色功能**
```java
// 特色功能-自动上传文件
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
        new AutoFileMsg(new File("C:\\Users\\51293\\Desktop\\surfear-test.txt"))
);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```

----------
**7. 语音类型**
```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
        new VoiceMsg(
                new Voice("3a8asd892asd8asd")
        )
);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```
**7.1 语音类型-特色功能**
```java
// 特色功能-自动上传语音
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
        new AutoFileMsg(new File("C:\\Users\\51293\\Downloads\\清脆的敲键盘声音.mp3"))
);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```

----------
**8. 模版卡片类型**
```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
        new TemplateCardMsg(JSONObject.parseObject(
                "{\n" +
                        "        \"card_type\":\"text_notice\",\n" +
                        "        \"source\":{\n" +
                        "            \"icon_url\":\"https://wework.qpic.cn/wwpic/252813_jOfDHtcISzuodLa_1629280209/0\",\n" +
                        "            \"desc\":\"企业微信\",\n" +
                        "            \"desc_color\":0\n" +
                        "        },\n" +
                        "        \"main_title\":{\n" +
                        "            \"title\":\"欢迎使用企业微信\",\n" +
                        "            \"desc\":\"您的好友正在邀请您加入企业微信\"\n" +
                        "        },\n" +
                        "        \"emphasis_content\":{\n" +
                        "            \"title\":\"100\",\n" +
                        "            \"desc\":\"数据含义\"\n" +
                        "        },\n" +
                        "        \"quote_area\":{\n" +
                        "            \"type\":1,\n" +
                        "            \"url\":\"https://work.weixin.qq.com/?from=openApi\",\n" +
                        "            \"appid\":\"APPID\",\n" +
                        "            \"pagepath\":\"PAGEPATH\",\n" +
                        "            \"title\":\"引用文本标题\",\n" +
                        "            \"quote_text\":\"Jack：企业微信真的很好用~\\nBalian：超级好的一款软件！\"\n" +
                        "        },\n" +
                        "        \"sub_title_text\":\"下载企业微信还能抢红包！\",\n" +
                        "        \"horizontal_content_list\":[\n" +
                        "            {\n" +
                        "                \"keyname\":\"邀请人\",\n" +
                        "                \"value\":\"张三\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"keyname\":\"企微官网\",\n" +
                        "                \"value\":\"点击访问\",\n" +
                        "                \"type\":1,\n" +
                        "                \"url\":\"https://work.weixin.qq.com/?from=openApi\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"keyname\":\"企微下载\",\n" +
                        "                \"value\":\"企业微信.apk\",\n" +
                        "                \"type\":2,\n" +
                        "                \"media_id\":\"3gNgBNS2u51DelpP8VviLEzTbIdBm1YRcLZNnjNlrRhfkPlftNcgB2mIp72YmSoqr\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"jump_list\":[\n" +
                        "            {\n" +
                        "                \"type\":1,\n" +
                        "                \"url\":\"https://work.weixin.qq.com/?from=openApi\",\n" +
                        "                \"title\":\"企业微信官网\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"card_action\":{\n" +
                        "                \"type\":1,\n" +
                        "                \"url\":\"https://work.weixin.qq.com/?from=openApi\",\n" +
                        "                \"title\":\"企业微信官网\"\n" +
                        "            }\n" +
                        "    }"
        ))
);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```
**8.1 模版卡片类型-模板填充**

在 `resources/templates/im` 目录下添加模板文件：`Wecom_模版卡片类型-文本通知模版卡片.json`及`Wecom_模版卡片类型-图文展示模版卡片.json`

`Wecom_模版卡片类型-文本通知模版卡片.json`
```json
{
  "card_type":"text_notice",
  "source":{
    "icon_url":"https://wework.qpic.cn/wwpic/252813_jOfDHtcISzuodLa_1629280209/0",
    "desc":"企业微信",
    "desc_color":0
  },
  "main_title":{
    "title":"欢迎使用${title}",
    "desc":"您的好友正在邀请您加入企业微信"
  },
  "emphasis_content":{
    "title":"100",
    "desc":"数据含义"
  },
  "quote_area":{
    "type":1,
    "url":"https://work.weixin.qq.com/?from=openApi",
    "title":"引用文本标题",
    "quote_text":"Jack：企业微信真的很好用~\nBalian：超级好的一款软件！"
  },
  "sub_title_text":"下载企业微信还能抢红包！",
  "horizontal_content_list":[
    {
      "keyname":"邀请人",
      "value":"张三"
    },
    {
      "keyname":"企微官网",
      "value":"点击访问",
      "type":1,
      "url":"https://work.weixin.qq.com/?from=openApi"
    }
  ],
  "jump_list":[
    {
      "type":1,
      "url":"https://work.weixin.qq.com/?from=openApi",
      "title":"企业微信官网"
    }
  ],
  "card_action":{
    "type":1,
    "url":"https://work.weixin.qq.com/?from=openApi"
  }
}
```

`Wecom_模版卡片类型-图文展示模版卡片.json`
```json
{
  "card_type":"news_notice",
  "source":{
    "icon_url":"https://wework.qpic.cn/wwpic/252813_jOfDHtcISzuodLa_1629280209/0",
    "desc":"企业微信",
    "desc_color":0
  },
  "main_title":{
    "title":"欢迎使用企业微信${title}",
    "desc":"您的好友正在邀请您加入企业微信"
  },
  "card_image":{
    "url":"https://wework.qpic.cn/wwpic/354393_4zpkKXd7SrGMvfg_1629280616/0",
    "aspect_ratio":2.25
  },
  "image_text_area":{
    "type":1,
    "url":"https://work.weixin.qq.com",
    "title":"欢迎使用企业微信",
    "desc":"您的好友正在邀请您加入企业微信",
    "image_url":"https://wework.qpic.cn/wwpic/354393_4zpkKXd7SrGMvfg_1629280616/0"
  },
  "quote_area":{
    "type":1,
    "url":"https://work.weixin.qq.com/?from=openApi",
    "title":"引用文本标题",
    "quote_text":"Jack：企业微信真的很好用~\nBalian：超级好的一款软件！"
  },
  "vertical_content_list":[
    {
      "title":"惊喜红包等你来拿",
      "desc":"下载企业微信还能抢红包！"
    }
  ],
  "horizontal_content_list":[
    {
      "keyname":"邀请人",
      "value":"张三"
    },
    {
      "keyname":"企微官网",
      "value":"点击访问",
      "type":1,
      "url":"https://work.weixin.qq.com/?from=openApi"
    }
  ],
  "jump_list":[
    {
      "type":1,
      "url":"https://work.weixin.qq.com/?from=openApi",
      "title":"企业微信官网"
    }
  ],
  "card_action":{
    "type":1,
    "url":"https://work.weixin.qq.com/?from=openApi"
  }
}
```

注意当前通道为`IM`类型，所以模板文件需要放在`resources/templates/im`目录下。

```java
WecomWebhookRobotModel model = new WecomWebhookRobotModel(
new TemplateCardMsg());

// model.setTemplateFileName("Wecom_模版卡片类型-文本通知模版卡片.json");
model.setTemplateFileName("Wecom_模版卡片类型-图文展示模版卡片.json");

Map<String, Object> params = new HashMap<>();
params.put("title", "顺风耳");
model.setTemplateParams(params);

String channelKey = "im:wecom:webhook:robot";
MessageSender.send(channelKey, model);
```

----------
**9. 文件上传接口**
```java
File file = new File("C:\\Users\\51293\\Desktop\\surfear-test.txt");

// 方式一：手动设置配置
// WecomWebhookRobotConfig config = new WecomWebhookRobotConfig("配置key");
// 方式二：从当前环境中获取配置
WecomWebhookRobotConfig config = ConfigUtil.getChannelConfigObject(null, WecomWebhookRobotConfig.class);

WecomWebhookUploadMediaApi uploadMediaApi = new WecomWebhookUploadMediaApi(config, "file", file);
JSONObject response = uploadMediaApi.parseResponse(new OkhttpClient().execute(uploadMediaApi));

String mediaId = response.getString("media_id");
System.out.println(mediaId);
```