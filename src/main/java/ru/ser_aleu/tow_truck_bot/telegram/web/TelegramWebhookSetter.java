package ru.ser_aleu.tow_truck_bot.telegram.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.ser_aleu.tow_truck_bot.telegram.web.dto.TelegramWebhookResponse;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramWebhookSetter {

    private final RestTemplate restTemplate;
    private final ApplicationContext appContext;
    private final ObjectMapper mapper;

    @Value("${telegram.web.auth.token}")
    private String telegramWebAuthToken;
    @Value("${telegram.web.hook.url}")
    private String webhookPath;
    @Value("${telegram.web.hook.secret}")
    private String webhookSecret;

//    @PostConstruct
    public void setTelegramWebhook() {
        TelegramWebhookResponse telegramWebhookResponse = null;
        try {
            String url = UriComponentsBuilder.fromUriString("https://api.telegram.org/bot" + telegramWebAuthToken + "/setWebhook")
                    .queryParam("url", webhookPath)
                    .queryParam("secret_token",webhookSecret)
                    .toUriString();
            ResponseEntity<String> response = restTemplate.postForEntity(url,null, String.class);
            telegramWebhookResponse = mapper.readValue(response.getBody(), TelegramWebhookResponse.class);
        } catch (Exception e) {
            log.error("Error while getting telegram webhook setting approve. {} ", getStackTrace(e));
        }
        if (telegramWebhookResponse == null || Boolean.FALSE.equals(telegramWebhookResponse.isOk())) {
            int exitCode = SpringApplication.exit(appContext, () -> 0);
            log.error("Telegram webhook wasn't set successfully. Application will stop working! Webhook url = {}, secret = {}", webhookPath, webhookSecret);
            System.exit(exitCode);
        }
        log.info("Telegram webhook was set successfully! Webhook setting status = {}, webhook url = {}, secret = {}", telegramWebhookResponse.isOk(), webhookPath, webhookSecret);
    }
}
