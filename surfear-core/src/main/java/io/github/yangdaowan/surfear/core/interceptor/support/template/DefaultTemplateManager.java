 package io.github.yangdaowan.surfear.core.interceptor.support.template;

 import io.github.yangdaowan.surfear.core.exception.TemplateException;
 import io.github.yangdaowan.surfear.core.interceptor.support.template.engine.TemplateEngine;
 import io.github.yangdaowan.surfear.core.interceptor.support.template.engine.ThymeleafTemplateEngine;
 import io.github.yangdaowan.surfear.core.interceptor.support.template.enums.TemplateSource;
 import io.github.yangdaowan.surfear.core.interceptor.support.template.manager.TemplateManager;
 import io.github.yangdaowan.surfear.core.interceptor.support.template.model.MessageTemplate;
 import io.github.yangdaowan.surfear.core.interceptor.support.template.service.TemplateDBService;

 import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 默认模板管理器实现
 * <p>
 * 提供模板的创建、更新、删除、查询和渲染功能。
 * 支持本地文件模板和数据库模板两种来源。
 * 线程安全的实现，支持并发访问。
 * </p>
 * 
 * @author ycf
 */
public class DefaultTemplateManager implements TemplateManager {

    private static volatile TemplateManager instance;
    private static final Object LOCK = new Object();

    /**
     * 获取单例实例（双重检查锁定）
     * 
     * @return 模板管理器实例
     */
    public static TemplateManager getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new DefaultTemplateManager();
                }
            }
        }
        return instance;
    }

    /**
     * 模板缓存映射，使用ConcurrentHashMap保证线程安全
     * key格式：messageType_templateName
     */
    private final Map<String, MessageTemplate> templateMap = new ConcurrentHashMap<>();
    
    /**
     * 读写锁，用于保护模板缓存的一致性
     * 读取操作可以并发，写入操作需要独占
     */
    private final ReadWriteLock cacheLock = new ReentrantReadWriteLock();
    private final TemplateDBService templateDBService;
    private final TemplateEngine templateEngine;

    /**
     * 默认构造函数
     * 使用默认的Thymeleaf模板引擎，不使用数据库服务
     */
    public DefaultTemplateManager() {
        this.templateDBService = null;
        this.templateEngine = new ThymeleafTemplateEngine();
        this.init();
    }

    /**
     * 完整构造函数
     * 
     * @param templateDBService 模板数据库服务
     * @param templateEngine 模板引擎
     */
    public DefaultTemplateManager(TemplateDBService templateDBService, TemplateEngine templateEngine) {
        this.templateDBService = templateDBService;
        this.templateEngine = templateEngine;
        this.init();
    }

    /**
     * 初始化模板缓存
     * 从本地文件和数据库加载模板到内存缓存中
     * TODO: 考虑添加动态加载机制，或者定时刷新缓存（如每10分钟）
     */
    private void init() {
        cacheLock.writeLock().lock();
        try {
            // 从本地进行初始化
            List<MessageTemplate> localMessageTemplates = TemplateLocalLoad.getInstance().getMessageTemplate();
            for (MessageTemplate messageTemplate : localMessageTemplates) {
                String key = buildTemplateKey(messageTemplate.getMessageType(), messageTemplate.getName());
                messageTemplate.setSource(TemplateSource.LOCAL.name());
                templateMap.put(key, messageTemplate);
            }

            // 从数据库进行初始化
            if (templateDBService != null) {
                List<MessageTemplate> dbMessageTemplates = templateDBService.getMessageTemplate();
                for (MessageTemplate template : dbMessageTemplates) {
                    String key = buildTemplateKey(template.getMessageType(), template.getName());
                    template.setSource(TemplateSource.DB.name());
                    templateMap.put(key, template);
                }
            }
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    /**
     * 构建模板缓存的key
     * 
     * @param messageType 消息类型
     * @param templateName 模板名称
     * @return 缓存key
     */
    private String buildTemplateKey(String messageType, String templateName) {
        return messageType + "_" + templateName;
    }

    @Override
    public String renderTemplate(MessageTemplate template, Map<String, Object> variables) {
        return templateEngine.processTemplate(template.getMode(), template.getContent(), variables);
    }

    @Override
    public String renderTemplate(String messageType, String templateMode, String templateName, Map<String, Object> variables) {
        MessageTemplate template = getTemplate(messageType, templateName);
        return templateEngine.processTemplate(templateMode, template.getContent(), variables);
    }

    @Override
    public MessageTemplate createTemplate(String source, String messageType, String templateMode, String templateName, String content) {
        if (source.equals(TemplateSource.LOCAL.name())) {
            throw new TemplateException("本地模板不支持操作");
        }
        
        cacheLock.writeLock().lock();
        try {
            if (source.equals(TemplateSource.DB.name()) && templateDBService != null) {
                templateDBService.createTemplate(source, messageType, templateMode, templateName, content);
            }
            MessageTemplate templateNew = new MessageTemplate(source, messageType, templateMode, templateName, content);
            String key = buildTemplateKey(messageType, templateName);
            templateMap.put(key, templateNew);
            return templateNew;
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    @Override
    public MessageTemplate updateTemplate(String messageType, String templateMode, String templateName, String content) {
        cacheLock.writeLock().lock();
        try {
            MessageTemplate template = getTemplate(messageType, templateName);
            String source = template.getSource();
            if (source.equals(TemplateSource.LOCAL.name())) {
                throw new TemplateException("本地模板不支持操作");
            }
            if (source.equals(TemplateSource.DB.name()) && templateDBService != null) {
                templateDBService.updateTemplate(messageType, templateMode, templateName, content);
            }
            MessageTemplate templateNew = new MessageTemplate(template.getSource(), messageType, templateMode, templateName, content);
            String key = buildTemplateKey(messageType, templateName);
            templateMap.put(key, templateNew);
            return templateNew;
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    @Override
    public void deleteTemplate(String messageType, String templateName) {
        cacheLock.writeLock().lock();
        try {
            MessageTemplate template = getTemplate(messageType, templateName);
            String source = template.getSource();
            if (source.equals(TemplateSource.LOCAL.name())) {
                throw new TemplateException("本地模板不支持操作");
            }
            if (source.equals(TemplateSource.DB.name()) && templateDBService != null) {
                templateDBService.deleteTemplate(messageType, templateName);
            }
            String key = buildTemplateKey(messageType, templateName);
            templateMap.remove(key);
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    @Override
    public MessageTemplate getTemplate(String messageType, String templateName) {
        cacheLock.readLock().lock();
        try {
            String key = buildTemplateKey(messageType, templateName);
            MessageTemplate template = templateMap.get(key);
            if (template != null) {
                return template;
            }
            throw new TemplateException("找不到对应模板文件：" + key);
        } finally {
            cacheLock.readLock().unlock();
        }
    }

    /**
     * 刷新模板缓存
     * 重新从本地文件和数据库加载所有模板
     */
    public void refreshCache() {
        cacheLock.writeLock().lock();
        try {
            templateMap.clear();
            init();
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    /**
     * 获取缓存中的模板数量
     * 
     * @return 模板数量
     */
    public int getCacheSize() {
        cacheLock.readLock().lock();
        try {
            return templateMap.size();
        } finally {
            cacheLock.readLock().unlock();
        }
    }



}