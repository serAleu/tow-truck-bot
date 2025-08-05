package ru.ser_aleu.tow_truck_bot.telegram.dto;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TelegramUserSessionRegistry {

    private final ConcurrentHashMap<Long, TelegramUser> userSessions = new ConcurrentHashMap<>();

    public TelegramUser getOrCreateUser(Long chatId) {
        return userSessions.computeIfAbsent(chatId, id -> new TelegramUser().setChatId(id));
    }

    public void updateUser(TelegramUser user) {
        userSessions.put(user.getChatId(), user);
    }
}