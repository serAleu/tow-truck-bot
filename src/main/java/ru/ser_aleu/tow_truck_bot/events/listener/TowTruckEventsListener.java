package ru.ser_aleu.tow_truck_bot.events.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.ser_aleu.tow_truck_bot.events.dto.*;
import ru.ser_aleu.tow_truck_bot.exceptions.EventProcessingException;
import ru.ser_aleu.tow_truck_bot.telegram.service.TelegramService;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Component
@Slf4j
@RequiredArgsConstructor
public class TowTruckEventsListener {

    private final TelegramService telegramService;

    @Async("eventTaskExecutor")
    @EventListener
    public void handleBotStartedEvent(BotStartedEvent event) {
        try {
            log.info("Processing bot started event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process bot started event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Bot started event processing failed.", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            log.info("Processing user registered event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process user registered event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("User registered processing failed", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleFraudDetectionEvent(FraudDetectionEvent event) {
        try {
            log.info("Processing fraud detection event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process fraud detection event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Fraud detection processing failed.", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleVehicleTypeSelectedEvent(VehicleTypeSelectedEvent event) {
        try {
            log.info("Processing vehicle type selected event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process vehicle type selected event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Vehicle type selected processing failed.", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleOrderCalculationEvent(OrderCalculationEvent event) {
        try {
            log.info("Processing order calculation event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process order calculation event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Order calculation processing failed.", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleLocationProvidedEvent(LocationProvidedEvent event) {
        try {
            log.info("Processing location provided event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process location provided event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Location providing processing failed", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleOrderConfirmedEvent(OrderConfirmedEvent event) {
        try {
            log.info("Processing order confirmed event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process order confirmed event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Order confirmed processing failed", e);
        }
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleOrderSentToOperatorEvent(OrderSentToOperatorEvent event) {
        try {
            log.info("Processing order sent to operator event: chat_id = {}, telegram_user = {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName());
        } catch (Exception e) {
            log.error("Failed to process order sent to operator event: chat_id = {}, telegram_user = {}, {}", event.getTelegramUser().getChatId(), event.getTelegramUser().getTelegramUserName(), getStackTrace(e));
            throw new EventProcessingException("Order sent to operator processing failed", e);
        }
    }
}
