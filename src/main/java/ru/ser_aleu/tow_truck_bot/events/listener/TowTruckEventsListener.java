package ru.ser_aleu.tow_truck_bot.events.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.ser_aleu.tow_truck_bot.events.dto.LocationProvidedEvent;
import ru.ser_aleu.tow_truck_bot.events.dto.OrderCalculationEvent;
import ru.ser_aleu.tow_truck_bot.exceptions.EventProcessingException;

@Component
@Slf4j
public class TowTruckEventsListener {

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
