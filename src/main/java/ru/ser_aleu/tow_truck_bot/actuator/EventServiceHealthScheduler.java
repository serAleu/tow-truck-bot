package ru.ser_aleu.tow_truck_bot.actuator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.ser_aleu.tow_truck_bot.notification.LogNotificationService;
import ru.ser_aleu.tow_truck_bot.notification.NotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventServiceHealthScheduler {

    private final ExecutorHealthIndicator executorHealthIndicator;
    private final LogNotificationService notificationService;

    // Проверка каждый час в начале часа (00:00, 01:00 и т.д.)
//    @Scheduled(cron = "0 0 * * * *")
    @Scheduled(fixedDelay = 360000)
    public void hourlyHealthCheck() {
        Health health = executorHealthIndicator.health();
        Status status = health.getStatus();

        log.info("Scheduled health check: Status = {}, Details = {}", status, health.getDetails());

        if (status.equals(Status.DOWN)) {
            handleCriticalFailure(health);
        } else if (health.getDetails().get("queueSize") != null &&
                (Integer)health.getDetails().get("queueSize") > 1000) {
            handleHighLoad(health);
        }
    }

    private void handleCriticalFailure(Health health) {
        String message = String.format(
                "🚨 CRITICAL: Event service health check failed! Status: %s, Details: %s",
                health.getStatus(),
                health.getDetails());

        notificationService.sendAlert(message);
        log.error(message);
    }

    private void handleHighLoad(Health health) {
        String message = String.format(
                "⚠️ WARNING: High load detected in event service! Queue size: %d, Active threads: %d",
                health.getDetails().get("queueSize"),
                health.getDetails().get("activeThreads"));

        notificationService.sendWarning(message);
        log.warn(message);
    }
}
