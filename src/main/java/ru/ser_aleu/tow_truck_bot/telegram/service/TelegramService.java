package ru.ser_aleu.tow_truck_bot.telegram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.ser_aleu.tow_truck_bot.telegram.TelegramUtils;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUserLocation;
import ru.ser_aleu.tow_truck_bot.telegram.web.TelegramMessageSender;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static ru.ser_aleu.tow_truck_bot.telegram.enums.Callback.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramService {

    @Value("${telegram.web.bot-username}")
    private String webTelegramBotUsername;
    @Value("${telegram.steps.error.text}")
    private String webTelegramErrorText;
    @Value("${telegram.web.admin-chat-id}")
    private Long webTelegramAdminChatId;

    private final TelegramMessageSender messageSender;
    private final TelegramUtils telegramUtils;
    private final Map<Long, Queue<Update>> chatQueues = new ConcurrentHashMap<>();

    public SendMessage processChatQueue(Map<Long, Queue<Update>> chatQueues, Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(webTelegramErrorText)
                .build();
        Queue<Update> queue = chatQueues.get(chatId);
        Update update;
        while ((update = queue.poll()) != null) {
            try {
                sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text("Processing chat_id = " + chatId + " text = " + update.getMessage().getText())
                        .build();
            } catch (Exception e) {
                log.error("Error processing update in chat {}, {}", chatId, getStackTrace(e));
            }
        }
        if (queue.isEmpty()) {
            chatQueues.remove(chatId, queue);
        }
        return sendMessage;
    }

    public void processRequestLocation(Update update) {
        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                .build();
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton locationButton = KeyboardButton.builder()
                .text("Поделиться местоположением")
                .requestLocation(true)
                .build();
        keyboardRow.add(locationButton);

        keyboardMarkup.setKeyboard(List.of(keyboardRow));

        SendMessage sendMessage = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text("Пожалуйста, поделитесь вашим местоположением")
                .replyMarkup(keyboardMarkup)
                .build();
        messageSender.sendMessage(sendMessage);
    }

    public void processReceivedLocation(Update update) {
        double latitude = update.getMessage().getLocation().getLatitude();
        double longitude = update.getMessage().getLocation().getLongitude();
        long chatId = update.getMessage().getChatId();

        // Выводим в консоль полученные координаты
        System.out.println("\n=== Получены координаты пользователя ===");
        System.out.println("Chat ID: " + chatId);
        System.out.println("Широта: " + latitude);
        System.out.println("Долгота: " + longitude);
        System.out.println("===============================\n");

        TelegramUserLocation userLocation = new TelegramUserLocation()
                .setLatitude(latitude)
                .setLongitude(longitude);

        // Здесь можно добавить вызов API 2GIS/Яндекс.Карт
        // calculateRoute(latitude, longitude);

        String responseText = String.format(
                "Ваше местоположение получено!\nКоординаты: %.6f, %.6f\n\n" +
                        "Теперь укажите тип транспортного средства (легковой/грузовой).",
                userLocation.getLatitude(), userLocation.getLongitude()
        );

        messageSender.sendMessage(SendMessage.builder()
                .chatId(chatId)
                .text(responseText)
                .build());
    }

    public void sendStartReply(Update update) {
        try {
            messageSender.sendMessage(telegramUtils.getStartBotReply(update));
            messageSender.sendMessage(telegramUtils.getVehicleTypeQuestion(update));
        } catch (Exception e) {
            log.error("Error while sending start reply. chatId = {}, {}", update.getMessage().getChatId(), getStackTrace(e));
        }
    }

    public void processTextRequest(Update update) {
        Long chatId = update.getMessage().getChatId();
        try {
            Queue<Update> queue = chatQueues.computeIfAbsent(chatId, id -> new ConcurrentLinkedQueue<>());
            queue.add(update);
            messageSender.sendMessage(processChatQueue(chatQueues, chatId));
        } catch (Exception e) {
            log.error("Error while processing text message. Message = {}, chatId = {}, {}", update.getMessage(), chatId, getStackTrace(e));
        }
    }

    private void sendStatistics(String userName, String request, String response) {
        SendMessage requestMessage = SendMessage.builder()
                .chatId(webTelegramAdminChatId)
                .text(userName + " - REQUEST: " + request + "\n" + "RESPONSE: " + response)
                .build();
//        execute(requestMessage);
    }
}
