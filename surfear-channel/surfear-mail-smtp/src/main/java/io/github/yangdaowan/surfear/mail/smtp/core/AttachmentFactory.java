package io.github.yangdaowan.surfear.mail.smtp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

/**
 * 邮件附件工厂类，提供从不同来源创建邮件附件的功能
 *
 * <p>支持以下附件来源：
 * <ul>
 *   <li>文件系统</li>
 *   <li>项目资源（resources目录）</li>
 *   <li>网络URL</li>
 *   <li>字节数组（动态生成内容）</li>
 * </ul>
 *
 * <p>所有临时文件将统一存储在系统临时目录下的mail_attachments子目录中
 *
 * @author ycf
 */
public final class AttachmentFactory {

    private static final Logger log = LoggerFactory.getLogger(AttachmentFactory.class);
    private static final String TEMP_DIR_PREFIX = "mail_attachments";
    private static final Set<Path> TEMP_FILES = new HashSet<>();
    private static final Path TEMP_ROOT_DIR;

    static {
        try {
            // 初始化临时目录
            TEMP_ROOT_DIR = Files.createTempDirectory(TEMP_DIR_PREFIX);
            TEMP_ROOT_DIR.toFile().deleteOnExit();
        } catch (IOException e) {
            throw new ExceptionInInitializerError("无法创建临时目录: " + e.getMessage());
        }
    }

    private AttachmentFactory() {
        // 工具类，防止实例化
    }

    /**
     * 清理所有创建的临时文件
     */
    public static void cleanupTempFiles() {
        synchronized (TEMP_FILES) {
            for (Path tempFile : TEMP_FILES) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    // 记录错误但继续删除其他文件
                    log.warn("删除临时文件失败: {}, {}", tempFile, e.getMessage());
                }
            }
            TEMP_FILES.clear();
        }
    }

    /**
     * 获取临时文件存储目录
     * @return 临时目录路径
     */
    public static Path getTempDirectory() {
        return TEMP_ROOT_DIR;
    }

    /**
     * 从文件系统创建邮件附件
     * @param filePath 文件系统路径（绝对或相对路径）
     * @return 包含文件数据的DataHandler
     * @throws IOException 如果文件不存在或读取失败
     */
    public static MailAttachment fromFileSystem(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("文件不存在: " + filePath);
        }
        if (!file.canRead()) {
            throw new IOException("文件不可读: " + filePath);
        }

        DataHandler dataHandler = new DataHandler(new FileDataSource(file));
        return new MailAttachment(file.getName(), dataHandler);
    }

    /**
     * 从项目资源创建邮件附件
     * @param resourcePath 资源路径（相对于classpath）
     * @param displayName 附件显示名称（带扩展名）
     * @return 包含资源数据的DataHandler
     * @throws IOException 如果资源不存在或读取失败
     */
    public static MailAttachment fromResource(String resourcePath, String displayName) throws IOException {
        if (resourcePath == null || resourcePath.trim().isEmpty()) {
            throw new IllegalArgumentException("资源路径不能为空");
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("显示名称不能为空");
        }

        try (InputStream is = AttachmentFactory.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("资源不存在: " + resourcePath);
            }

            Path tempFile = createTempFile(displayName);
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
            registerTempFile(tempFile);

            DataHandler dataHandler = new DataHandler(new FileDataSource(tempFile.toFile()));
            return new MailAttachment(displayName, dataHandler);
        }
    }

    /**
     * 从网络URL创建邮件附件
     * @param fileUrl 文件URL地址
     * @param displayName 附件显示名称（带扩展名）
     * @return 包含网络资源的DataHandler
     * @throws IOException 如果URL访问失败或下载失败
     */
    public static MailAttachment fromUrl(String fileUrl, String displayName) throws IOException {
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL不能为空");
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("显示名称不能为空");
        }

        URL url = new URL(fileUrl);
        Path tempFile = createTempFile(displayName);

        try (InputStream is = url.openStream()) {
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
            registerTempFile(tempFile);

            DataHandler dataHandler = new DataHandler(new FileDataSource(tempFile.toFile()));
            return new MailAttachment(displayName, dataHandler);
        }
    }

    /**
     * 从字节数组创建邮件附件（适用于动态生成的内容）
     * @param data 附件内容字节数组
     * @param fileName 附件文件名（带扩展名）
     * @param contentType 可选的内容类型（如"text/csv"），如果为null则根据文件名推断
     * @return 包含字节数据的DataHandler
     */
    public static MailAttachment fromBytes(byte[] data, String fileName, String contentType) {
        if (data == null) {
            throw new IllegalArgumentException("数据字节数组不能为空");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        String mimeType = (contentType != null && contentType.contains("/"))
                ? contentType
                : determineContentType(fileName);

        DataHandler dataHandler =  new DataHandler(new ByteArrayDataSource(data, mimeType));
        return new MailAttachment(fileName, dataHandler);
    }

    /**
     * 创建临时文件并注册
     * @param displayName 显示名称（用于生成文件名）
     * @return 创建的临时文件路径
     * @throws IOException 如果创建文件失败
     */
    private static Path createTempFile(String displayName) throws IOException {
        // 清理文件名中的非法字符
        String safeName = displayName.replaceAll("[^a-zA-Z0-9.-]", "_");
        String prefix = safeName.substring(0, Math.min(safeName.length(), 10));
        String suffix = safeName.contains(".")
                ? safeName.substring(safeName.lastIndexOf('.'))
                : ".tmp";

        return Files.createTempFile(TEMP_ROOT_DIR, prefix + "_", suffix);
    }

    /**
     * 注册临时文件以便后续清理
     * @param tempFile 临时文件路径
     */
    private static void registerTempFile(Path tempFile) {
        tempFile.toFile().deleteOnExit();
        synchronized (TEMP_FILES) {
            TEMP_FILES.add(tempFile);
        }
    }

    /**
     * 根据文件扩展名确定内容类型
     * @param fileName 文件名
     * @return 对应的MIME类型
     */
    private static String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "csv":  return "text/csv";
            case "pdf":  return "application/pdf";
            case "doc":  return "application/msword";
            case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":  return "application/vnd.ms-excel";
            case "xlsx": return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt":  return "application/vnd.ms-powerpoint";
            case "pptx": return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "jpg": case "jpeg": return "image/jpeg";
            case "png":  return "image/png";
            case "gif":  return "image/gif";
            case "html": case "htm": return "text/html";
            case "txt":  return "text/plain";
            case "zip":  return "application/zip";
            case "json": return "application/json";
            case "xml":  return "application/xml";
            default:     return "application/octet-stream";
        }
    }
}