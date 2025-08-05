package ru.ser_aleu.tow_truck_bot.telegram.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ser_aleu.tow_truck_bot.events.EventPublisher;
import ru.ser_aleu.tow_truck_bot.events.dto.*;
import ru.ser_aleu.tow_truck_bot.telegram.TelegramUtils;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.service.TelegramService;

import static ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState.*;

@Slf4j
@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramWebhookController {

    @Value("${telegram.web.hook.secret}")
    private String webhookSecret;

    private final TelegramService telegramService;
    private final EventPublisher eventPublisher;
    private final TelegramUtils telegramUtils;

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhookUpdate(@RequestBody Update update,
                                                 @RequestHeader("X-Telegram-Bot-Api-Secret-Token") String receivedSecretToken) {
        if(isNeedToProcessUpdate(update, receivedSecretToken)) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                if ("/start".equalsIgnoreCase(update.getMessage().getText())) {
                    eventPublisher.publishBotStartedEvent(new BotStartedEvent(telegramUtils.createOrGetTelegramUser(update, update.getMessage().getChatId(), START)));
                } else {
                    eventPublisher.publishTextMessageEvent(new TextMessageEvent(telegramUtils.createOrGetTelegramUser(update, update.getMessage().getChatId(), PUT_TEXT)));
                }
            } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null) {
                if("VEHICLE_TYPE=".equalsIgnoreCase(update.getCallbackQuery().getData())) {
                    eventPublisher.publishVehicleTypeSelectedEvent(new VehicleTypeSelectedEvent(telegramUtils.createOrGetTelegramUser(update, update.getMessage().getChatId(), AWAITING_VEHICLE_TYPE_PROBLEM_SELECTION)));
                }
                log.info("update.hasMessage() = {}, update.getCallbackQuery().getData() = {}, update.getCallbackQuery().getMessage().getChatId() = {}", update.hasMessage(), update.getCallbackQuery().getData(), update.getCallbackQuery().getMessage().getChatId());
            } else if (update.hasMessage() && update.getMessage().hasLocation()) {
                eventPublisher.publishLocationProvidedEvent(new LocationProvidedEvent(telegramUtils.createOrGetTelegramUser(update, update.getCallbackQuery().getMessage().getChatId(), PUT_TEXT)));
            }
        }
        return ResponseEntity.ok().build();
    }

    private boolean isNeedToProcessUpdate(Update update, String receivedSecretToken) {
        if(webhookSecret.equalsIgnoreCase(receivedSecretToken)) {
            return (update.getMessage() != null && update.getMessage() != null && update.getMessage().getChatId() != null) || (update.hasCallbackQuery());
        } else {
            log.error("Request with an invalid token was received. Received token = {}", receivedSecretToken);
            eventPublisher.publishErrorAdminNotificationEvent(new ErrorAdminNotificationEvent(new TelegramUser()).setReceivedSecretToken(receivedSecretToken));
            return false;
        }
    }
}
