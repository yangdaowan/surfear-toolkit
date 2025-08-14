package io.github.yangdaowan.surfear.im.dingtalk.core.store;

public interface AccessTokenStore {

    /**
     * 获取token
     */
    String getAccessToken(String appKey);

    /**
     * 缓存token
     * @param appKey 应用key
     * @param accessToken token
     * @param expiresInSeconds 过期时间，秒
     */
    void setAccessToken(String appKey, String accessToken, long expiresInSeconds);

    /**
     * token是否存在
     */
    boolean isExist(String appKey);

    /**
     * 清除token
     */
    void delete(String appKey);

    /**
     * 清空
     */
    void clearAll();

}
