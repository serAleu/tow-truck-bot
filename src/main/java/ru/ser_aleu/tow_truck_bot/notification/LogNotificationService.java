package ru.ser_aleu.tow_truck_bot.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!prod")
public class LogNotificationService implements NotificationService {
    @Override
    public void sendAlert(String message) {
        log.error("[ALERT] {}", message);
    }

    @Override
    public void sendWarning(String message) {
        log.warn("[WARNING] {}", message);
    }
}