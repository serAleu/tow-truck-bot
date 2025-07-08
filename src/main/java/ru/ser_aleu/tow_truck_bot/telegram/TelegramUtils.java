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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUserLocation;
import ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static ru.ser_aleu.tow_truck_bot.telegram.enums.Callback.START;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramUtils {

    private final List<String> forbiddenWords;

    @Value("${telegram.steps.start.button}")
    private String telegramStepsStartButton;
    @Value("${telegram.steps.start.message}")
    private String telegramStepsStartMessage;
    @Value("${telegram.steps.start.image-path}")
    private String telegramStepsStartImagePath;
    @Value("${telegram.steps.options.dishes.button}")
    private String telegramStepsOptionsDishesButton;
    @Value("${telegram.steps.options.option1.button}")
    private String telegramStepsOptionsOption1Button;
    @Value("${telegram.steps.options.option2.button}")
    private String telegramStepsOptionsOption2Button;

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

    public void defineMessageText(TelegramUser user, SendMessage message) {
//        switch (user.getCurrentCommunicationStep() != null ? user.getCurrentCommunicationStep() : NO_RESPONSE) {
//            case STUPID_GIGA_RESPONSE -> message.setText(TELEGRAM_USERS_MAP.get(user.getChatId()).getUserName() + ", " + TELEGRAM_USERS_MAP.get(user.getChatId()).getCommunications().get(STUPID_GIGA_RESPONSE));
//            case STUPID_USER_RESPONSE -> message.setText(TELEGRAM_USERS_MAP.get(user.getChatId()).getUserName() + ", " + TELEGRAM_USERS_MAP.get(user.getChatId()).getCommunications().get(STUPID_USER_RESPONSE));
//            case DISHES_RESPONSE -> message.setText(TELEGRAM_USERS_MAP.get(user.getChatId()).getUserName() + ", вот, что я для тебя нашел: " + TELEGRAM_USERS_MAP.get(user.getChatId()).getCommunications().get(DISHES_RESPONSE));
//            default -> message.setText(TELEGRAM_USERS_MAP.get(user.getChatId()).getUserName() + ", " + TELEGRAM_USERS_MAP.get(user.getChatId()).getCommunications().get(NO_RESPONSE));
//        }
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

    public SendPhoto startBot(Update update) {
        try {
            String startMessage = "Привет, " + update.getMessage().getFrom().getFirstName() + "! " + telegramStepsStartMessage;
            return SendPhoto.builder()
                    .chatId(update.getMessage().getChatId())
                    .photo(new InputFile(new File(telegramStepsStartImagePath)))
                    .caption(startMessage)
                    .replyMarkup(getStartButton())
                    .build();
        } catch (Exception e) {
            log.error("Exception while start-message with photo sending. e = {}", getStackTrace(e));
            return SendPhoto.builder()
                    .chatId(update.getMessage().getChatId())
                    .caption("Привет, " + update.getMessage().getFrom().getFirstName() + "! " + "Эта сучка сломалась.")
                    .build();
        }
    }

    public InlineKeyboardMarkup getStartButton() {
        InlineKeyboardButton startButton = InlineKeyboardButton.builder()
                .callbackData(START.getPath())
                .text(telegramStepsStartButton)
                .build();
        InlineKeyboardRow inlineKeyboardRow = new InlineKeyboardRow();
        inlineKeyboardRow.add(startButton);
        return InlineKeyboardMarkup.builder()
                .keyboardRow(inlineKeyboardRow)
                .build();
    }
}