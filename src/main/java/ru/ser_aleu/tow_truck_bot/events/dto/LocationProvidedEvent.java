package ru.ser_aleu.tow_truck_bot.events.dto;

import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUserLocation;

public class LocationProvidedEvent extends BaseEvent {
    private TelegramUserLocation telegramUserLocation;
}
