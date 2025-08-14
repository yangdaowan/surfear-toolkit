package io.github.yangdaowan.surfear.core.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 线程池工厂，统一管理线程资源
 * @author ycf
 */
@Slf4j
public final class ExecutorFactory {

    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private static final int MAX_POOL_SIZE = CORE_POOL_SIZE * 2;
    private static final long KEEP_ALIVE_TIME = 60L;
    private static final int QUEUE_CAPACITY = 1000;

    private static volatile ThreadPoolExecutor defaultExecutor;
    private static volatile ScheduledExecutorService scheduledExecutor;

    private ExecutorFactory() {}

    public static ThreadPoolExecutor getDefaultExecutor() {
        if (defaultExecutor == null) {
            synchronized (ExecutorFactory.class) {
                if (defaultExecutor == null) {
                    defaultExecutor = new ThreadPoolExecutor(
                        CORE_POOL_SIZE,
                        MAX_POOL_SIZE,
                        KEEP_ALIVE_TIME,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(QUEUE_CAPACITY),
                        new ThreadFactoryBuilder()
                            .setNameFormat("surfear-pool-%d")
                            .setUncaughtExceptionHandler((t, e) -> log.error("Thread {} uncaught exception", t.getName(), e))
                            .build(),
                        new ThreadPoolExecutor.CallerRunsPolicy()
                    );
                }
            }
        }
        return defaultExecutor;
    }

    public static ExecutorService createThreadPoolExecutor(String nameFormat) {
        return Executors.newFixedThreadPool(
                CORE_POOL_SIZE,
                new ThreadFactoryBuilder()
                        .setNameFormat(nameFormat)
                        .setUncaughtExceptionHandler((t, e) -> log.error("Thread {} uncaught exception", t.getName(), e))
                        .build()
        );
    }


    public static ScheduledExecutorService getScheduledExecutor() {
        if (scheduledExecutor == null) {
            synchronized (ExecutorFactory.class) {
                if (scheduledExecutor == null) {
                    scheduledExecutor = Executors.newScheduledThreadPool(
                        CORE_POOL_SIZE,
                        new ThreadFactoryBuilder()
                            .setNameFormat("surfear-scheduled-%d")
                            .setUncaughtExceptionHandler((t, e) -> log.error("Thread {} uncaught exception", t.getName(), e))
                            .build()
                    );
                }
            }
        }
        return scheduledExecutor;
    }

    /**
     * 创建通道专用线程池
     * @param namePrefix 线程名称前缀
     * @param coreSize 核心线程数
     * @param maxSize 最大线程数
     * @param queueSize 队列大小
     * @return 线程池
     */
    public static ThreadPoolExecutor createChannelExecutor(String namePrefix, int coreSize, int maxSize, int queueSize) {
        return new ThreadPoolExecutor(
            coreSize,
            maxSize,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(queueSize),
            new ThreadFactoryBuilder()
                .setNameFormat(namePrefix + "-%d")
                .setUncaughtExceptionHandler((t, e) -> log.error("Thread {} uncaught exception", t.getName(), e))
                .build(),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public static void shutdown() {
        if (defaultExecutor != null) {
            defaultExecutor.shutdown();
            try {
                if (!defaultExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                    defaultExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                defaultExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        if (scheduledExecutor != null) {
            scheduledExecutor.shutdown();
            try {
                if (!scheduledExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduledExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduledExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 获取线程池运行状态
     */
    public static String getStatus() {
        if (defaultExecutor == null) {
            return "Not initialized";
        }
        return String.format(
            "ActiveCount: %d, CompletedTaskCount: %d, TaskCount: %d, QueueSize: %d",
            defaultExecutor.getActiveCount(),
            defaultExecutor.getCompletedTaskCount(),
            defaultExecutor.getTaskCount(),
            defaultExecutor.getQueue().size()
        );
    }
}
