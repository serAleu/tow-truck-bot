package ru.ser_aleu.tow_truck_bot.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.ser_aleu.tow_truck_bot.notification.email.EmailService;
import ru.ser_aleu.tow_truck_bot.notification.slack.SlackService;

@Slf4j
@Component
@Profile("prod")
@RequiredArgsConstructor
public class ProdNotificationService implements NotificationService {
    private final EmailService emailService;
    private final SlackService slackService;

    @Override
    public void sendAlert(String message) {
        emailService.send("devops@company.com", "Event Service Alert", message);
        slackService.sendToChannel("#alerts", message);
    }

    @Override
    public void sendWarning(String message) {
        log.error("Warning was sent. {}", message);
    }

}