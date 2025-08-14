package io.github.yangdaowan.surfear.im.dingtalk.core.store;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ycf
 **/
public class InMemoryAccessTokenStore implements AccessTokenStore {

    private final ConcurrentHashMap<String, TokenEntry> tokenStore = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
    public InMemoryAccessTokenStore() {
        // 每1小时清理一次过期Token
        cleanupExecutor.scheduleAtFixedRate(
                this::removeExpiredTokens,
                1, 1, TimeUnit.HOURS
        );
    }

    @Override
    public String getAccessToken(String appKey) {
        TokenEntry entry = tokenStore.get(appKey);
        if (entry == null) {
            return null;
        }
        // 未过期直接返回
        if (!entry.isExpired()) {
            return entry.accessToken;
        }

        // 过期时进入同步块
        synchronized (this) {
            // 第二次有锁检查（可能已被其他线程更新）
            entry = tokenStore.get(appKey);
            if (entry == null) {
                return null;
            }

            if (entry.isExpired()) {
                tokenStore.remove(appKey); // 确认过期后删除
                return null;
            }
            return entry.accessToken; // 其他线程已更新，返回新Token
        }
    }

    @Override
    public void setAccessToken(String appKey, String accessToken, long expiryTime) {
        Objects.requireNonNull(appKey);
        Objects.requireNonNull(accessToken);
        if (expiryTime <= 0) throw new IllegalArgumentException("到期时间必须为正数");

        long realExpiryTime = System.currentTimeMillis() + expiryTime * 1000;
        tokenStore.put(appKey, new TokenEntry(accessToken, realExpiryTime));
    }

    @Override
    public boolean isExist(String appKey) {
        TokenEntry entry = tokenStore.get(appKey);
        return entry != null && !entry.isExpired();
    }

    @Override
    public void delete(String appKey) {
        tokenStore.remove(appKey);
    }

    @Override
    public void clearAll() {
        tokenStore.clear();
    }

    private void removeExpiredTokens() {
        tokenStore.entrySet().removeIf(entry ->
                entry.getValue().isExpired()
        );
    }

    private static class TokenEntry {
        String accessToken;
        long expiryTime;

        TokenEntry(String accessToken, long expiryTime) {
            this.accessToken = accessToken;
            this.expiryTime = expiryTime;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
}