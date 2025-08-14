package io.github.yangdaowan.surfear.core.interceptor.support.template;

import io.github.yangdaowan.surfear.core.exception.TemplateException;
import io.github.yangdaowan.surfear.core.interceptor.support.template.enums.TemplateMode;
import io.github.yangdaowan.surfear.core.interceptor.support.template.enums.TemplateSource;
import io.github.yangdaowan.surfear.core.interceptor.support.template.model.MessageTemplate;
import io.github.yangdaowan.surfear.core.interceptor.support.template.service.TemplateLoad;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemplateLocalLoad implements TemplateLoad {

    private static final TemplateLocalLoad instance = new TemplateLocalLoad();
    private static final Set<String> MESSAGE_TYPE_NAMES = Arrays.stream(MessageType.values())
            .map(Enum::name)
            .map(String::toLowerCase)
            .collect(Collectors.toCollection(HashSet::new));
    private static final Set<String> SUPPORTED_SUFFIXES = Arrays.stream(TemplateMode.values())
            .map(Enum::name)
            .map(String::toLowerCase)
            .collect(Collectors.toCollection(HashSet::new));
    private static final String TEMPLATE_DIR = "templates";

    @Override
    public List<MessageTemplate> getMessageTemplate() {
        return readTemplates();
    }

    private List<MessageTemplate> readTemplates() {
        List<MessageTemplate> templates = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            Enumeration<URL> resources = classLoader.getResources(TEMPLATE_DIR);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                templates.addAll(processResourceUrl(url));
            }
        } catch (IOException e) {
            throw new TemplateException("模板资源读取失败", e);
        }
        return templates;
    }

    private List<MessageTemplate> processResourceUrl(URL url) {
        try {
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                return processFileDirectory(url);
            } else if ("jar".equals(protocol)) {
                return processJarFile(url);
            }
            return Collections.emptyList();
        } catch (IOException | URISyntaxException e) {
            throw new TemplateException("模板处理资源 URL 时出错："+ url, e);
        }
    }

    private List<MessageTemplate> processFileDirectory(URL url) throws URISyntaxException, IOException {
        Path templateDir = Paths.get(url.toURI());
        try (Stream<Path> paths = Files.walk(templateDir, 2)) {
            return paths.filter(Files::isRegularFile)
                    .map(this::parsePath)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    private List<MessageTemplate> processJarFile(URL url) throws IOException {
        List<MessageTemplate> templates = new ArrayList<>();
        JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
        String jarFilePath = URLDecoder.decode(jarConnection.getJarFileURL().getFile(), StandardCharsets.UTF_8.name());
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().startsWith(TEMPLATE_DIR + "/")) {
                    MessageTemplate template = parseJarEntry(jarFile, entry);
                    if (template != null) {
                        templates.add(template);
                    }
                }
            }
        }
        return templates;
    }

    private MessageTemplate parsePath(Path filePath) {
        // 将路径转换为绝对路径字符串进行处理
        String fullPath = filePath.toAbsolutePath().toString();
        try {
            String templateDirPattern = Pattern.quote(File.separator + TEMPLATE_DIR + File.separator);
            String[] parts = fullPath.split(templateDirPattern);

            if (parts.length < 2) return null;

            String relativePath = parts[1];
            String[] pathSegments = relativePath.split(Pattern.quote(File.separator));

            if (pathSegments.length < 2) return null;

            String messageType = pathSegments[0];
            String fileName = pathSegments[1];
            String suffix = subFileNameSuffix(fileName);
            if (!isValidMessageType(messageType) || !isSupportedSuffix(suffix)) {
                return null;
            }

            String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            return new MessageTemplate(TemplateSource.LOCAL.name(), messageType, TemplateMode.of(suffix), fileName, content);
        } catch (Exception e) {
            throw new TemplateException("模板处理资源 PATH 时出错："+ fullPath, e);
        }
    }

    private String subFileNameSuffix(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex != -1
                ? fileName.substring(lastDotIndex + 1)
                : null;
    }

    private MessageTemplate parseJarEntry(JarFile jarFile, JarEntry entry) {
        String[] pathSegments = entry.getName().split("/");
        if (pathSegments.length < 3) return null;

        String messageType = pathSegments[1];
        String fileName = pathSegments[pathSegments.length - 1];
        String suffix = subFileNameSuffix(fileName);

        if (!isValidMessageType(messageType) || !isSupportedSuffix(suffix)) {
            return null;
        }

        try (InputStream is = jarFile.getInputStream(entry)) {
            String content = new String(readAllBytes(is), StandardCharsets.UTF_8);
            return new MessageTemplate(TemplateSource.LOCAL.name(),messageType, TemplateMode.of(suffix), fileName, content);
        } catch (IOException e) {
            throw new TemplateException("模板读取 JAR 条目时出错："+ entry.getName(), e);
        }
    }

    private boolean isValidMessageType(String type) {
        return MESSAGE_TYPE_NAMES.contains(type.toLowerCase());
    }

    private boolean isSupportedSuffix(String suffix) {
        if(suffix == null) return false;
        return SUPPORTED_SUFFIXES.contains(suffix.toLowerCase());
    }

    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384]; // 使用16KB的缓冲区

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

    public static TemplateLocalLoad getInstance() {
        return instance;
    }
}