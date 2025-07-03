package ru.ser_aleu.tow_truck_bot.telegram.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUserSessionRegistry;

@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramWebhookController {

    private final TelegramUserSessionRegistry sessionRegistry;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/webhook2")
    public ResponseEntity<?> handleWebhookUpdate() {
        System.out.println("GEtttttt");
//        if (update.hasMessage()) {
//            processMessage(update.getMessage());
//        } else if (update.hasCallbackQuery()) {
//            processCallback(update.getCallbackQuery());
//        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhookUpdate(@RequestBody Update update,
                                                 @RequestHeader("X-Telegram-Bot-Api-Secret-Token") String secretToken) {
        System.out.println("Хуя струя, secret = " + secretToken);
//        if (update.hasMessage()) {
//            processMessage(update.getMessage());
//        } else if (update.hasCallbackQuery()) {
//            processCallback(update.getCallbackQuery());
//        }
        return ResponseEntity.ok().build();
    }

    private void processMessage(Message message) {
        Long chatId = message.getChatId();
        TelegramUser user = sessionRegistry.getOrCreateUser(chatId);

        if (message.hasText()) {
//            eventPublisher.publishEvent(new TextMessageEvent(user, message.getText()));
        } else if (message.hasLocation()) {
//            user.setUserLocation(convertLocation(message.getLocation()));
//            eventPublisher.publishEvent(new LocationMessageEvent(user));
        }
        // ... другие типы сообщений (фото, документы и т.д.)
    }

    private void processCallback(CallbackQuery callback) {
        // Обработка inline-кнопок
//        eventPublisher.publishEvent(new CallbackEvent(callback));
    }
}
