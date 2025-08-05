package ru.ser_aleu.tow_truck_bot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUserLocation;
import ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramUtils {

    private final List<String> forbiddenWords;
    private final InlineKeyboardMarkup vehicleTypeKeyboard;

    @Value("${telegram.steps.questions.vehicle-type.text}")
    private String telegramVehicleTypeText;
    @Value("${telegram.steps.start.message}")
    private String telegramStepsStartMessage;
    @Value("${telegram.steps.start.image-path}")
    private String telegramStepsStartImagePath;

    public TelegramUser createTelegramUser(Update update, ChatState chatState) {
        TelegramUser telegramUser = new TelegramUser()
                .setCurrentChatState(chatState);
        if (update != null && update.getMessage() != null && update.getMessage().getChatId() != null) {
            telegramUser.setUpdate(update)
                    .setChatId(update.getMessage().getChatId())
                    .setTelegramUserName(update.getMessage().getFrom() != null && update.getMessage().getFrom().getUserName() != null ? update.getMessage().getFrom().getUserName() : null)
                    .setUserName(update.getMessage().getFrom() != null && update.getMessage().getFrom().getLastName() != null ? update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName() : null)
                    .setPhoneNumber(update.getMessage().getFrom() != null && update.getMessage().getContact() != null && update.getMessage().getContact().getPhoneNumber() != null ? update.getMessage().getContact().getPhoneNumber() : null)
                    .setUserLocation(new TelegramUserLocation()
                            .setLatitude(update.getMessage().getLocation() != null ? update.getMessage().getLocation().getLatitude() : null)
                            .setLongitude(update.getMessage().getLocation() != null ? update.getMessage().getLocation().getLongitude() : null));
        }
        return telegramUser;
    }

    public boolean isRequestContainForbiddenWord(String requestText) {
        AtomicBoolean isContain = new AtomicBoolean(false);
        forbiddenWords.forEach(forbiddenWord -> {
            if (StringUtils.containsIgnoreCase(requestText, forbiddenWord)) {
                isContain.set(true);
            }
        });
        return isContain.get();
    }

    public SendPhoto getStartBotReply(Update update) {
        try {
            return SendPhoto.builder()
                    .chatId(update.getMessage().getChatId())
                    .photo(new InputFile(new File(telegramStepsStartImagePath)))
                    .caption(telegramStepsStartMessage)
                    .build();
        } catch (Exception e) {
            log.error("Exception while start-message with photo sending. {}", getStackTrace(e));
            return null;
        }
    }

    public SendMessage getVehicleTypeQuestion(Update update) {
        try {
            return SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(telegramVehicleTypeText)
                    .replyMarkup(vehicleTypeKeyboard)
                    .build();
        } catch (Exception e) {
            log.error("Exception while vehicle type question creating. {}", getStackTrace(e));
            return null;
        }
    }
}