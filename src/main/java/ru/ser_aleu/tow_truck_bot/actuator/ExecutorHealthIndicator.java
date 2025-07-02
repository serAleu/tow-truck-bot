package ru.ser_aleu.tow_truck_bot.actuator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
public class ExecutorHealthIndicator implements HealthIndicator {
    private final ThreadPoolTaskExecutor eventTaskExecutor;

    public ExecutorHealthIndicator(@Qualifier("eventTaskExecutor") Executor eventTaskExecutor) {
        this.eventTaskExecutor = (ThreadPoolTaskExecutor) eventTaskExecutor;
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail("activeThreads", eventTaskExecutor.getActiveCount())
                .withDetail("queueSize", eventTaskExecutor.getThreadPoolExecutor().getQueue().size())
                .withDetail("completedTasks", eventTaskExecutor.getThreadPoolExecutor().getCompletedTaskCount())
                .build();
    }
}