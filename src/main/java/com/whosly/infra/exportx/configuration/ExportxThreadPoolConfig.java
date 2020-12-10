package com.whosly.infra.exportx.configuration;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.whosly.infra.exportx.metadata.config.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * spring init， single instance
 */
@Configuration
public class ExportxThreadPoolConfig implements Configurable {
    public static final String NAME = "com.whosly.infra.exportx.exportxThreadPool";

    @Bean(NAME)
    public ExecutorService buildExcelThreadPool() {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1000);

        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("excel-pool-%d")
                .build();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10 * cpuNum, 30 * cpuNum,
                1, TimeUnit.MINUTES, workQueue, threadFactory);

        return MoreExecutors.getExitingExecutorService(threadPoolExecutor);
    }
}
