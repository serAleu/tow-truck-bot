package ru.ser_aleu.tow_truck_bot.events.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.ser_aleu.tow_truck_bot.events.dto.LocationProvidedEvent;
import ru.ser_aleu.tow_truck_bot.events.dto.OrderCalculationEvent;
import ru.ser_aleu.tow_truck_bot.exceptions.EventProcessingException;
import ru.ser_aleu.tow_truck_bot.telegram.service.TelegramService;

@Component
@Slf4j
@RequiredArgsConstructor
public class TowTruckEventsListener {

    private final TelegramService telegramService;

    @Async("eventTaskExecutor")
    @EventListener
    public void handleOrderCalculationEvent(OrderCalculationEvent event) {
        log.info("Processing order creation: {}", event.getChatId());

        try {
            processOrderCreation(event);
        } catch (Exception e) {
            log.error("Failed to process order creation", e);
            throw new EventProcessingException("Order creation processing failed", e);
        }
    }

    private void processOrderCreation(OrderCalculationEvent event) {
        // Бизнес-логика обработки
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void handleLocationProvidedEvent(LocationProvidedEvent event) {
        // Аналогичная структура обработки
    }
}
