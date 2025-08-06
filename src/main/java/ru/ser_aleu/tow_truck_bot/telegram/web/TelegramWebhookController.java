package ru.ser_aleu.tow_truck_bot.telegram.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ser_aleu.tow_truck_bot.events.EventPublisher;
import ru.ser_aleu.tow_truck_bot.events.dto.*;
import ru.ser_aleu.tow_truck_bot.telegram.TelegramUtils;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUserSessionRegistry;
import ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.VehicleProblemType;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.VehicleType;
import ru.ser_aleu.tow_truck_bot.telegram.service.TelegramService;

import static ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState.*;

@Slf4j
@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramWebhookController {

    @Value("${telegram.web.hook.secret}")
    private String webhookSecret;

    private final EventPublisher eventPublisher;
    private final TelegramUserSessionRegistry telegramUserSessionRegistry;

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhookUpdate(@RequestBody Update update,
                                                 @RequestHeader("X-Telegram-Bot-Api-Secret-Token") String receivedSecretToken) {
        if(isNeedToProcessUpdate(update, receivedSecretToken)) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                if ("/start".equalsIgnoreCase(update.getMessage().getText())) {
                    eventPublisher.publishBotStartedEvent(new BotStartedEvent(telegramUserSessionRegistry.createOrGetTelegramUser(update, update.getMessage().getChatId())));
                } else {
                    eventPublisher.publishTextMessageEvent(new TextMessageEvent(telegramUserSessionRegistry.createOrGetTelegramUser(update, update.getMessage().getChatId())));
                }
            } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null) {
                if(StringUtils.containsIgnoreCase(update.getCallbackQuery().getData(), "VEHICLE_TYPE=")) {
                    eventPublisher.publishVehicleTypeSelectedEvent(new VehicleTypeSelectedEvent(telegramUserSessionRegistry.createOrGetTelegramUser(update, update.getCallbackQuery().getMessage().getChatId()), VehicleType.valueOf(StringUtils.substringAfter(update.getCallbackQuery().getData(), "="))));
                }
                if(StringUtils.containsIgnoreCase(update.getCallbackQuery().getData(), "VEHICLE_PROBLEM_TYPE=")) {
                    eventPublisher.publishVehicleProblemTypeSelectedEvent(new VehicleProblemTypeSelectedEvent(telegramUserSessionRegistry.createOrGetTelegramUser(update, update.getCallbackQuery().getMessage().getChatId()), VehicleProblemType.valueOf(StringUtils.substringAfter(update.getCallbackQuery().getData(), "="))));
                }
            } else if (update.hasMessage() && update.getMessage().hasLocation()) {
                eventPublisher.publishLocationProvidedEvent(new LocationProvidedEvent(telegramUserSessionRegistry.createOrGetTelegramUser(update, update.getMessage().getChatId())));
            }
        }
        return ResponseEntity.ok().build();
    }

    private boolean isNeedToProcessUpdate(Update update, String receivedSecretToken) {
        if(webhookSecret.equalsIgnoreCase(receivedSecretToken)) {
            return (update.getMessage() != null && update.getMessage() != null && update.getMessage().getChatId() != null) || (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null);
        } else {
            log.error("Request with an invalid token was received. Received token = {}", receivedSecretToken);
            eventPublisher.publishErrorAdminNotificationEvent(new ErrorAdminNotificationEvent(new TelegramUser()).setReceivedSecretToken(receivedSecretToken));
            return false;
        }
    }
}
