package ru.ser_aleu.tow_truck_bot.telegram.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhookUpdate(@RequestBody Update update) {
        System.out.println("Хуя струя");
        if (update.hasMessage()) {
            processMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            processCallback(update.getCallbackQuery());
        }
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
