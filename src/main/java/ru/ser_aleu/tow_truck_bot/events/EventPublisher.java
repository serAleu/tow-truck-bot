package ru.ser_aleu.tow_truck_bot.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.ser_aleu.tow_truck_bot.events.dto.LocationProvidedEvent;
import ru.ser_aleu.tow_truck_bot.events.dto.OrderCalculationEvent;

@RequiredArgsConstructor
@Slf4j
@Service
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishOrderCalculationEvent(OrderCalculationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishLocationProvidedEvent(LocationProvidedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
