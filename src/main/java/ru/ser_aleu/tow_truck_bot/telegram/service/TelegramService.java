package ru.ser_aleu.tow_truck_bot.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.ser_aleu.tow_truck_bot.telegram.TelegramUtils;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.web.telegram.TelegramMessageSender;

import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Service
@Slf4j
public class TelegramService {

    @Value("${telegram.web.bot-username}")
    private String webTelegramBotUsername;
    @Value("${telegram.steps.error.text}")
    private String webTelegramErrorText;
    @Value("${telegram.web.admin-chat-id}")
    private Long webTelegramAdminChatId;
    @Value("${telegram.steps.start.message}")
    private String telegramStepsStartMessage;
    @Value("${telegram.steps.start.image-path}")
    private String telegramStepsStartImagePath;
    @Value("${telegram.steps.questions.vehicle-type.text}")
    private String telegramVehicleTypeText;
    @Value("${telegram.steps.questions.vehicle-problem.text}")
    private String telegramVehicleProblemText;
    @Value("${telegram.steps.questions.current-location.text}")
    private String telegramCurrentLocationText;
    @Value("${telegram.steps.questions.destination-location.text}")
    private String telegramDestinationLocationText;
    @Value("${telegram.steps.questions.payment-method.text}")
    private String telegramPaymentMethodText;
    @Value("${telegram.steps.questions.need-docs.text}")
    private String telegramNeedDocsText;
    @Value("${telegram.steps.questions.order-confirmation.text}")
    private String telegramOrderConfirmationText;
    @Value("${telegram.steps.questions.final-reply.text}")
    private String telegramFinalReplyText;
    @Value("${telegram.steps.questions.text-reply.text1}")
    private String telegramTextReplyText1;
    @Value("${telegram.steps.questions.text-reply.text2}")
    private String telegramTextReplyText2;
    @Value("${telegram.steps.questions.phone-num.text}")
    private String telegramPhoneNumText;
    @Value("${telegram.steps.questions.phone-num.error}")
    private String telegramPhoneNumError;

    private final TelegramMessageSender messageSender;
    private final TelegramUtils telegramUtils;
    private final InlineKeyboardMarkup vehicleTypeKeyboard;
    private final InlineKeyboardMarkup vehicleProblemTypeKeyboard;
    private final InlineKeyboardMarkup paymentMethodTypeKeyboard;
    private final InlineKeyboardMarkup yesOrNoKeyboard;
    private final InlineKeyboardMarkup confirmationKeyBoard;

    public TelegramService(TelegramMessageSender messageSender, TelegramUtils telegramUtils,
                           @Qualifier("vehicleTypeKeyboard") InlineKeyboardMarkup vehicleTypeKeyboard,
                           @Qualifier("vehicleProblemTypeKeyboard") InlineKeyboardMarkup vehicleProblemTypeKeyboard,
                           @Qualifier("paymentMethodTypeKeyboard") InlineKeyboardMarkup paymentMethodTypeKeyboard,
                           @Qualifier("yesOrNoKeyboard") InlineKeyboardMarkup yesOrNoKeyboard,
                           @Qualifier("confirmationKeyBoard") InlineKeyboardMarkup confirmationKeyBoard) {
        this.messageSender = messageSender;
        this.telegramUtils = telegramUtils;
        this.vehicleTypeKeyboard = vehicleTypeKeyboard;
        this.vehicleProblemTypeKeyboard = vehicleProblemTypeKeyboard;
        this.paymentMethodTypeKeyboard = paymentMethodTypeKeyboard;
        this.yesOrNoKeyboard = yesOrNoKeyboard;
        this.confirmationKeyBoard = confirmationKeyBoard;
    }

    public void sendRequestCurrentLocation(TelegramUser telegramUser) {
        try {
            ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                    .oneTimeKeyboard(true)
                    .resizeKeyboard(true)
                    .build();
            KeyboardButton locationButton = KeyboardButton.builder()
                    .text("Поделиться местоположением")
                    .requestLocation(true)
                    .build();
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(locationButton);
            keyboardMarkup.setKeyboard(List.of(keyboardRow));

            messageSender.sendMessage(telegramUtils.getSendMessageWithKeyboard(telegramUser, telegramCurrentLocationText, keyboardMarkup));
        } catch (Exception e) {
            log.error("Error while sending request current location. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    public void sendRequestDestinationLocation(TelegramUser telegramUser) {
        try {
            messageSender.sendMessage(telegramUtils.getSendMessage(telegramUser, telegramDestinationLocationText));
        } catch (Exception e) {
            log.error("Error while sending request destination location. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    public void sendFinalReply(TelegramUser telegramUser) {
        try {
            messageSender.sendMessage(telegramUtils.getSendMessage(telegramUser, telegramFinalReplyText));
        } catch (Exception e) {
            log.error("Error while sending final reply. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    public void sendStartReply(TelegramUser telegramUser) {
        try {
            messageSender.sendMessage(telegramUtils.getSendPhoto(telegramUser, telegramStepsStartImagePath, telegramStepsStartMessage));
        } catch (Exception e) {
            log.error("Error while sending start reply. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    public void sendVehicleTypeRequest(TelegramUser telegramUser) {
        try {
            messageSender.sendMessage(telegramUtils.getSendMessageWithKeyboard(telegramUser, telegramVehicleTypeText, vehicleTypeKeyboard));
        } catch (Exception e) {
            log.error("Error while sending vehicle type request. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    public void sendNeedDocsRequest(TelegramUser telegramUser) {
        try {
            messageSender.sendMessage(telegramUtils.getSendMessageWithKeyboard(telegramUser, telegramNeedDocsText, yesOrNoKeyboard));
        } catch (Exception e) {
            log.error("Error while sending need docs request. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    public void sendVehicleProblemRequest(TelegramUser telegramUser) {
        try {
            messageSender.sendMessage(telegramUtils.getSendMessageWithKeyboard(telegramUser, telegramVehicleProblemText, vehicleProblemTypeKeyboard));
        } catch (Exception e) {
            log.error("Error while sending vehicle problem request. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    public void sendIncorrectPhoneNumNotification(TelegramUser telegramUser) {
        try {
            messageSender.sendMessage(telegramUtils.getSendMessage(telegramUser, telegramPhoneNumError));
        } catch (Exception e) {
            log.error("Error while incorrect phone-num processing. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    public void sendPhoneNumRequest(TelegramUser telegramUser) {
        try {
            messageSender.sendMessage(telegramUtils.getSendMessage(telegramUser, telegramPhoneNumText));
        } catch (Exception e) {
            log.error("Error while phone num request processing. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    public void sendPaymentMethodRequest(TelegramUser telegramUser) {
        try {
            messageSender.sendMessage(telegramUtils.getSendMessageWithKeyboard(telegramUser, telegramPaymentMethodText, paymentMethodTypeKeyboard));
        } catch (Exception e) {
            log.error("Error while sending payment method request. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    public void sendOrderConfirmation(TelegramUser telegramUser) {
        try {
            messageSender.sendMessage(telegramUtils.getSendMessageWithKeyboard(telegramUser, telegramOrderConfirmationText  + '\n' + '\n' + telegramUser.toString(), confirmationKeyBoard));
        } catch (Exception e) {
            log.error("Error while sending result. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    public void processTextRequest(TelegramUser telegramUser) {
        try {
            String message = telegramUser.getCurrentChatState() != null ? telegramTextReplyText2 : telegramTextReplyText1;
            messageSender.sendMessage(telegramUtils.getSendMessage(telegramUser, message));
        } catch (Exception e) {
            log.error("Error while text request processing. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }

    private void sendStatistics(String userName, String request, String response) {
        SendMessage requestMessage = SendMessage.builder()
                .chatId(webTelegramAdminChatId)
                .text(userName + " - REQUEST: " + request + "\n" + "RESPONSE: " + response)
                .build();
//        execute(requestMessage);
    }

    public void sendOrderToAdmin(TelegramUser telegramUser) {
        try {
            messageSender.sendMessage(telegramUtils.getSendAdminMessage(webTelegramAdminChatId, telegramUser.toString()));
        } catch (Exception e) {
            log.error("Error while sending order to admin. chatId = {}, {}", telegramUser.getChatId(), getStackTrace(e));
        }
    }
}
