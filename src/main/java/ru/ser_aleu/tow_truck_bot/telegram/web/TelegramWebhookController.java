package ru.ser_aleu.tow_truck_bot.telegram.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ser_aleu.tow_truck_bot.events.EventPublisher;
import ru.ser_aleu.tow_truck_bot.events.dto.BotStartedEvent;
import ru.ser_aleu.tow_truck_bot.events.dto.ErrorAdminNotificationEvent;
import ru.ser_aleu.tow_truck_bot.events.dto.LocationProvidedEvent;
import ru.ser_aleu.tow_truck_bot.events.dto.TextMessageEvent;
import ru.ser_aleu.tow_truck_bot.telegram.TelegramUtils;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUserSessionRegistry;
import ru.ser_aleu.tow_truck_bot.telegram.service.TelegramService;

import static ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState.*;

@Slf4j
@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramWebhookController {

    @Value("${telegram.web.hook.secret}")
    private String webhookSecret;

    private final TelegramUserSessionRegistry sessionRegistry;
    private final TelegramService telegramService;
    private final EventPublisher eventPublisher;
    private final TelegramUtils telegramUtils;

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhookUpdate(@RequestBody Update update,
                                                 @RequestHeader("X-Telegram-Bot-Api-Secret-Token") String receivedSecretToken) {
        if(isNeedToProcessUpdate(update, receivedSecretToken)) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                if ("/start".equalsIgnoreCase(update.getMessage().getText())) {
                    eventPublisher.publishBotStartedEvent(new BotStartedEvent(telegramUtils.createTelegramUser(update, START)));
                } else {
                    eventPublisher.publishTextMessageEvent(new TextMessageEvent(telegramUtils.createTelegramUser(update, PUT_TEXT)));
                }
            } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null) {
//                processButtonPush(update);
            } else if (update.hasMessage() && update.getMessage().hasLocation()) {
                eventPublisher.publishLocationProvidedEvent(new LocationProvidedEvent(telegramUtils.createTelegramUser(update, PUT_TEXT)));
            }
        }
        return ResponseEntity.ok().build();
    }

    private boolean isNeedToProcessUpdate(Update update, String receivedSecretToken) {
        if(webhookSecret.equalsIgnoreCase(receivedSecretToken)) {
            return update.getMessage() != null && update.getMessage() != null && update.getMessage().getChatId() != null;
        } else {
            log.error("Request with an invalid token was received. Received token = {}", receivedSecretToken);
            eventPublisher.publishErrorAdminNotificationEvent(new ErrorAdminNotificationEvent(new TelegramUser()).setReceivedSecretToken(receivedSecretToken));
            return false;
        }
    }
}
