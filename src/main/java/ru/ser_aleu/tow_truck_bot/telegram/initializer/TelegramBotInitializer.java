//package ru.ser_aleu.tow_truck_bot.telegram.initializer;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import ru.ser_aleu.tow_truck_bot.telegram.TelegramMessageProcessor;
//
//import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
//
//@Component
//@DependsOn({"telegramMessageProcessor"})
//@RequiredArgsConstructor
//@Slf4j
//public class TelegramBotInitializer {
//
//    private final TelegramBotsApi telegramBotsApi;
//    private final TelegramMessageProcessor telegramMessageProcessor;
//
//    @PostConstruct
//    public void init() {
//        try {
//            telegramBotsApi.registerBot(telegramMessageProcessor);
//        } catch (TelegramApiException e) {
//            log.error("Error while tow-truck-bot initialization. {}", getStackTrace(e));
//        }
//    }
//}