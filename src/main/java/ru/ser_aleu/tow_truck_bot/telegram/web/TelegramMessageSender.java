package ru.ser_aleu.tow_truck_bot.telegram.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@RequiredArgsConstructor
public class TelegramMessageSender {

    private final TelegramClient telegramClient;

    public void sendMessage(SendMessage sendMessage) {
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    public void sendMessage(SendPhoto sendPhoto) {
        try {
            telegramClient.execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send photo", e);
        }
    }
}
