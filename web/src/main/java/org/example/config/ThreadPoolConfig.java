package org.example.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    private static int PC_CORES = Runtime.getRuntime().availableProcessors();

    static {
        if (PC_CORES >= 10)
            PC_CORES = 20;
        else
            PC_CORES = PC_CORES << 1 + 1;
    }

    /**
     * 更新线程池 io
     */
    @Bean(name = "UpdateDataThreadPool")
    public ThreadPoolExecutor updateDataThreadPool() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("UPDATE_POOL-%d")
                .setDaemon(true)
                .build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                PC_CORES,
                PC_CORES,
                600L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10000),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
}
