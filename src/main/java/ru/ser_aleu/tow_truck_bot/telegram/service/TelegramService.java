package ru.ser_aleu.tow_truck_bot.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUserLocation;
import ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState;

import java.util.List;
import java.util.Map;
import java.util.Queue;

@Service
@Slf4j
public class TelegramService {

    public SendMessage processChatQueue(Map<Long, Queue<Update>> chatQueues, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        Queue<Update> queue = chatQueues.get(chatId);
        Update update;
        while ((update = queue.poll()) != null) {
            try {
                processSingleUpdate(update, sendMessage);
            } catch (Exception e) {
                log.error("Error processing update in chat " + chatId, e);
            }
        }

        // Удаляем пустую очередь для освобождения памяти
        if (queue.isEmpty()) {
            chatQueues.remove(chatId, queue);
        }
        return sendMessage;
    }

    public SendMessage requestLocation(Update update) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton locationButton = new KeyboardButton();
        locationButton.setText("Поделиться местоположением");
        locationButton.setRequestLocation(true);
        keyboardRow.add(locationButton);

        keyboardMarkup.setKeyboard(List.of(keyboardRow));

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText("Пожалуйста, поделитесь вашим местоположением");
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    public SendMessage processReceivedLocation(Update update) {
        double latitude = update.getMessage().getLocation().getLatitude();
        double longitude = update.getMessage().getLocation().getLongitude();
        long chatId = update.getMessage().getChatId();

        // Выводим в консоль полученные координаты
        System.out.println("\n=== Получены координаты пользователя ===");
        System.out.println("Chat ID: " + chatId);
        System.out.println("Широта: " + latitude);
        System.out.println("Долгота: " + longitude);
        System.out.println("===============================\n");

        // Здесь можно добавить вызов API 2GIS/Яндекс.Карт
        // calculateRoute(latitude, longitude);

        // Отправляем подтверждение пользователю
        return sendConfirmation(chatId, new TelegramUserLocation()
                .setLatitude(latitude)
                .setLongitude(longitude)
                .setCurrentState(ChatState.LOCATION_RECEIVED));
    }

    private SendMessage sendConfirmation(Long chatId, TelegramUserLocation userLocation) {
        String responseText = String.format(
                "Ваше местоположение получено!\nКоординаты: %.6f, %.6f\n\n" +
                        "Теперь укажите тип транспортного средства (легковой/грузовой).",
                userLocation.getLatitude(), userLocation.getLongitude()
        );

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(responseText);
        return message;
    }

    private void processSingleUpdate(Update update, SendMessage message) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        message.setChatId(chatId);
        message.setText("Processing chat_id = " + chatId + " text = " + text);
    }
}
