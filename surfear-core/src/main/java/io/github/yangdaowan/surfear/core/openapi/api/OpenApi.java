package io.github.yangdaowan.surfear.core.openapi.api;

import com.google.common.base.Joiner;
import io.github.yangdaowan.surfear.core.exception.SendFailedException;
import io.github.yangdaowan.surfear.core.openapi.bean.FileUpload;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author ycf
 **/
@Slf4j
@Getter
@Setter
public class OpenApi {

    // 协议
    protected String protocol = "https";
    // host
    protected String host;
    // 请求路径，当资源路径为空时，使用正斜杠(/)作为CanonicalURI
    protected String canonicalUri;
    // httpMethod
    protected String httpMethod;
    // body参数
    protected String body;
    // headers
    protected Map<String, String> headers = new TreeMap<>();
    // query参数
    protected Map<String, Object> queryParam = new TreeMap<>();
    // 文件上传
    private FileUpload fileUpload;

    public OpenApi() {
        this.headers.put("User-Agent", userAgent());
    }

    public String buildUrl() {
        StringBuilder urlBuilder = new StringBuilder(protocol)
                .append("://")
                .append(host)
                .append(canonicalUri.startsWith("/") ? canonicalUri : "/" + canonicalUri);

        if (!queryParam.isEmpty()) {
            urlBuilder.append("?");
            queryParam.forEach((key, value) ->
                    urlBuilder.append(key).append("=").append(value).append("&"));
            urlBuilder.deleteCharAt(urlBuilder.length() - 1); // 移除最后一个&
        }

        String url = urlBuilder.toString();
        log.debug("RequestURL: {}", url);
        return url;
    }

    private String userAgent() {
        String language = System.getProperty("user.language", "");
        String region = System.getProperty("user.region", "");

        return Joiner.on('/').join(
                "surfear-message",
                System.getProperty("os.name", "").replace(' ', '_'),
                System.getProperty("os.version", "").replace(' ', '_'),
                System.getProperty("java.vm.name", "").replace(' ', '_'),
                System.getProperty("java.vm.version", "").replace(' ', '_'),
                System.getProperty("java.version", "").replace(' ', '_'),
                language != null ? language.replace(' ', '_') : "",
                region != null ? region.replace(' ', '_') : ""
        );
    }

    /**
     * 携带请求头(Body)必须设置 请求内容类型
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.headers.put("Content-Type", contentType);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void putQuery(String key, String value) {
        this.queryParam.put(key, value);
    }

    public void setUrl(String url) {
        try {
            URI uri = new URI(url);

            this.protocol = uri.getScheme();
            this.host = uri.getHost();
            this.canonicalUri = uri.getPath();

        } catch (URISyntaxException e) {
            log.error("Invalid URL: {}", e.getMessage());
            throw new SendFailedException("Invalid URL", e);
        }
    }

    public void setApplicationJson() {
        this.headers.put("Content-Type", "application/json; charset=utf-8");
        this.headers.put("Accept", "application/json; charset=utf-8");
    }

    public void setPostMethod(){
        this.httpMethod = "POST";
    }

    public void setGetMethod(){
        this.httpMethod = "GET";
    }

    public void setPost(String url){
        this.setPostMethod();
        this.setUrl(url);
    }

    public void setGet(String url){
        this.setGetMethod();
        this.setUrl(url);
    }

    public void setPostJson(String url){
        this.setApplicationJson();
        this.setPost(url);
    }

    public void setGetJson(String url){
        this.setApplicationJson();
        this.setGet(url);
    }

    /**
     * 设置文件上传的 multipart 请求
     */
    public void setMultipartFile(FileUpload fileUpload) {
        this.setPostMethod();
        this.fileUpload = fileUpload;
        this.setContentType("multipart/form-data; boundary=" + generateBoundary());
        this.body = null; // 清空原有body
    }

    public String generateBoundary(){
        return "-------------------------" + UUID.randomUUID().toString().replace("-", "");
    }
}
