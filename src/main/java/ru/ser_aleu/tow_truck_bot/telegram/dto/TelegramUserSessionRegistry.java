package ru.ser_aleu.tow_truck_bot.telegram.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class TelegramUserSessionRegistry {

    private final ConcurrentHashMap<Long, TelegramUser> userSessions = new ConcurrentHashMap<>();

    public TelegramUser createOrGetTelegramUser(Update update, Long chatId) {
        TelegramUser telegramUser = getOrCreateUser(chatId);
        if (telegramUser == null || telegramUser.getCurrentChatState() == null) {
            telegramUser = new TelegramUser()
                    .setCurrentChatState(null)
                    .setUpdate(update)
                    .setChatId(chatId)
                    .setTelegramUserName(update.getMessage().getFrom() != null && update.getMessage().getFrom().getUserName() != null ? update.getMessage().getFrom().getUserName() : null)
                    .setUserName(update.getMessage().getFrom() != null && update.getMessage().getFrom().getLastName() != null ? update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName() : null)
                    .setPhoneNumber(update.getMessage().getFrom() != null && update.getMessage().getContact() != null && update.getMessage().getContact().getPhoneNumber() != null ? update.getMessage().getContact().getPhoneNumber() : null)
                    .setCurrentLocation(new TelegramUserLocation(
                            update.getMessage().getLocation() != null ? update.getMessage().getLocation().getLatitude() : null,
                            update.getMessage().getLocation() != null ? update.getMessage().getLocation().getLongitude() : null
                    ));
            log.info("New telegram user was created: {}", telegramUser.toString());
        }
        return telegramUser;
    }

    public TelegramUser getOrCreateUser(Long chatId) {
        return userSessions.computeIfAbsent(chatId, id -> new TelegramUser().setChatId(id));
    }

    public void updateUser(TelegramUser user, String message) {
        userSessions.put(user.getChatId(), user);
        log.info("User info has been updated. chat_id: {}, username: {}, state: {}", user.getChatId(), user.getTelegramUserName(), message);
    }
}