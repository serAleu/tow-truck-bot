package ru.ser_aleu.tow_truck_bot.telegram;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.service.TelegramService;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static ru.ser_aleu.tow_truck_bot.telegram.enums.Callback.*;

@Slf4j
@RequiredArgsConstructor
@Service("telegramMessageProcessor")
public class TelegramMessageProcessor extends TelegramLongPollingBot {

    public static final Map<Long, TelegramUser> TELEGRAM_USERS_MAP = new HashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final Map<Long, Queue<Update>> chatQueues = new ConcurrentHashMap<>();

    private final String telegramBotToken;
    private final TelegramUtils telegramUtils;
    private final TelegramService telegramService;

    @Value("${telegram.web.bot-username}")
    private String webTelegramBotUsername;
    @Value("${telegram.web.admin-chat-id}")
    private Long webTelegramAdminChatId;
    @Value("${telegram.steps.options.message}")
    private String telegramStepsOptionsMessage;
    @Value("${telegram.steps.options.dishes.message}")
    private String telegramStepsOptionsDishesMessage;

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if(update.getMessage().getChatId() != null) {
                executor.execute(() -> {
                    if (update.hasMessage() && update.getMessage().hasText()) {
                        processTextRequest(update);
                        SendMessage message = telegramService.requestLocation(update);
                        extractedExecute(message);
                    } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null) {
                        processButtonPush(update);
                    } else if (update.hasMessage() && update.getMessage().hasLocation()) {
                        SendMessage sendMessage = telegramService.processReceivedLocation(update);
                        extractedExecute(sendMessage);
                    }
                });
            }
        } catch (Exception e) {
            log.error("Exception while telegram processing message. {}", getStackTrace(e));
        }
    }

    private void extractedExecute(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void processButtonPush(Update update) {
        SendMessage message = new SendMessage();
        telegramUtils.addUser(update, update.getCallbackQuery().getFrom().getId());
        message.setChatId(update.getCallbackQuery().getFrom().getId());
        String callback = update.getCallbackQuery().getData();
        if (callback.equalsIgnoreCase(START.getPath())) {
            message.setText(telegramStepsOptionsMessage);
            message.setReplyMarkup(telegramUtils.getOptionsButtons());
        } else if (callback.equalsIgnoreCase(DISHES.getPath())) {
//            telegramUtils.updateTelegramUserMap(null, SLOVOTBIRATOR_REQUEST, TELEGRAM_USERS_MAP.get(update.getCallbackQuery().getFrom().getId()));
            message.setText(telegramStepsOptionsDishesMessage);
        } else if (callback.equalsIgnoreCase(OPTION1.getPath())) {
            message.setText("не нажимай");
        } else if (callback.equalsIgnoreCase(OPTION2.getPath())) {
            message.setText("не нажимай сюда блять");
        } else {
            message.setText("пиздец");
        }
        extractedExecute(message);
    }

    private void processTextRequest(Update update) {
        Long chatId = update.getMessage().getChatId();
        try {
            Queue<Update> queue = chatQueues.computeIfAbsent(chatId, id -> new ConcurrentLinkedQueue<>());
            queue.add(update);
            if (update.getMessage().getText().equals("/start")) {
                execute(telegramUtils.startBot(update));
                return;
            }
            execute(telegramService.processChatQueue(chatQueues, chatId));
        } catch (Exception e) {
            log.error("Error while processing text message. Message = {}, chatId = {}, {}", update.getMessage(), chatId, getStackTrace(e));
        }
    }

    private void processUserMainRequest(Update update, SendMessage message) {
        Long chatId = update.getMessage().getChatId();
//        if(TELEGRAM_USERS_MAP.get(chatId).getCurrentCommunicationStep().equals(SLOVOTBIRATOR_REQUEST)) {
//            gigachatClientService.askGigachatSlovotbirator(TELEGRAM_USERS_MAP.get(chatId));
//        }
//        if (TELEGRAM_USERS_MAP.get(chatId).getCurrentCommunicationStep().equals(DISHES_REQUEST)) {
//            gigachatClientService.askGigachatDishes(TELEGRAM_USERS_MAP.get(chatId));
//        }
        telegramUtils.defineMessageText(TELEGRAM_USERS_MAP.get(chatId), message);
    }

    private void sendStatistics(String userName, String request, String response) throws TelegramApiException {
        SendMessage requestMessage = new SendMessage();
        requestMessage.setChatId(webTelegramAdminChatId);
        requestMessage.setText(userName + " - REQUEST: " + request + "\n" + "RESPONSE: " + response);
        execute(requestMessage);
        TELEGRAM_USERS_MAP.values().forEach(System.out::println);
    }

    @Override
    public String getBotUsername() {
        return webTelegramBotUsername;
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }
}
