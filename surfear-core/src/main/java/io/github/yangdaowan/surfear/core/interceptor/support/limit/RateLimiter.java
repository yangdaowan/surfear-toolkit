package io.github.yangdaowan.surfear.core.interceptor.support.limit;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 简单的滑动窗口限流器
 * @author ycf
 */
@Slf4j
public class RateLimiter {
    private final int limit;
    private final long windowSizeMillis;
    private final AtomicInteger currentCount;
    private final AtomicLong windowStart;

    public RateLimiter(int limit, long windowSizeMillis) {
        this.limit = limit;
        this.windowSizeMillis = windowSizeMillis;
        this.currentCount = new AtomicInteger(0);
        this.windowStart = new AtomicLong(System.currentTimeMillis());
    }

    public boolean tryAcquire() {
        long now = System.currentTimeMillis();
        long start = windowStart.get();

        // 检查是否需要滑动窗口
        if (now - start >= windowSizeMillis) {
            synchronized (this) {
                // 双重检查，确保线程安全
                if (now - windowStart.get() >= windowSizeMillis) {
                    currentCount.set(0);
                    windowStart.set(now);
                }
            }
        }

        // 尝试增加计数
        int count = currentCount.incrementAndGet();
        if (count <= limit) {
            return true;
        } else {
            currentCount.decrementAndGet();
            return false;
        }
    }

    public boolean tryAcquireWithWait(long maxWaitMillis) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < maxWaitMillis) {
            if (tryAcquire()) {
                return true;
            }
            try {
                Thread.sleep(100); // 短暂休眠避免空转
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }
}
