package ru.ser_aleu.tow_truck_bot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramUtils {

    private final List<String> forbiddenWords;

    public boolean isRequestContainForbiddenWord(String requestText) {
        AtomicBoolean isContain = new AtomicBoolean(false);
        forbiddenWords.forEach(forbiddenWord -> {
            if (StringUtils.containsIgnoreCase(requestText, forbiddenWord)) {
                isContain.set(true);
            }
        });
        return isContain.get();
    }

    public SendPhoto getSendPhoto(TelegramUser telegramUser, String imagePath, String message) {
        try {
            return SendPhoto.builder()
                    .chatId(telegramUser.getChatId())
                    .photo(new InputFile(new File(imagePath)))
                    .caption(message)
                    .build();
        } catch (Exception e) {
            log.error("Exception while SendPhoto creating. {}", getStackTrace(e));
            return null;
        }
    }

    public SendMessage getSendMessageWithKeyboard(TelegramUser telegramUser, String text, ReplyKeyboard keyboardMarkup) {
        try {
            return SendMessage.builder()
                    .chatId(telegramUser.getChatId())
                    .text(text)
                    .replyMarkup(keyboardMarkup)
                    .build();
        } catch (Exception e) {
            log.error("Exception while SendMessage with keyboard creating. {}", getStackTrace(e));
            return null;
        }
    }

    public SendMessage getSendMessage(TelegramUser telegramUser, String text) {
        try {
            return SendMessage.builder()
                    .chatId(telegramUser.getChatId())
                    .text(text)
                    .build();
        } catch (Exception e) {
            log.error("Exception while SendMessage creating. {}", getStackTrace(e));
            return null;
        }
    }
}