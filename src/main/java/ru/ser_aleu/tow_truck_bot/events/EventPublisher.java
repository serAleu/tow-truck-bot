package ru.ser_aleu.tow_truck_bot.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.ser_aleu.tow_truck_bot.events.dto.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishBotStartedEvent(BotStartedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishUserRegisteredEvent(UserRegisteredEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishFraudDetectionEvent(FraudDetectionEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishVehicleTypeSelectedEvent(VehicleTypeSelectedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishOrderCalculationEvent(OrderCalculationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishLocationProvidedEvent(LocationProvidedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishOrderConfirmedEvent(OrderConfirmedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishOrderSentToOperatorEvent(OrderSentToOperatorEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishErrorAdminNotificationEvent(ErrorAdminNotificationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishTextMessageEvent(TextMessageEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
