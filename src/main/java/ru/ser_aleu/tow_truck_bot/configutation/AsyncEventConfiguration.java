package ru.ser_aleu.tow_truck_bot.configutation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncEventConfiguration {

    private static final int CORE_POOL_SIZE = 50;
    private static final int MAX_POOL_SIZE = 100;
    private static final int QUEUE_CAPACITY = 500;
    private static final int AWAIT_TERMINATION_SECONDS = 30;
    private static final String THREAD_NAME_PREFIX = "Event-exec-";

    @Bean(name = "eventTaskExecutor")
    public Executor eventTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        configureExecutor(executor);
        configureShutdownPolicy(executor);

        executor.initialize();
        return executor;
    }

    private void configureExecutor(ThreadPoolTaskExecutor executor) {
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.setAllowCoreThreadTimeOut(true);
    }

    private void configureShutdownPolicy(ThreadPoolTaskExecutor executor) {
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }
}