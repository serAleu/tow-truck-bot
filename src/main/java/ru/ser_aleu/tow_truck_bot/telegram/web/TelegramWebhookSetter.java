package ru.ser_aleu.tow_truck_bot.telegram.web;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TelegramWebhookSetter {

    private final RestTemplate restTemplate;

    @Value("${telegram.web.auth.token}")
    private String telegramWebAuthToken;

    @Value("${telegram.webhook-url}")
    private String webhookPath;

    @PostConstruct
    public void setTelegramWebhook() {
        restTemplate.postForObject("https://api.telegram.org/bot" + telegramWebAuthToken + "/setWebhook?url=" + webhookPath, null, String.class);
    }
}
