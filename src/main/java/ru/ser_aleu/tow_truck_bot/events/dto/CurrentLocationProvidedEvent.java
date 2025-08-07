package ru.ser_aleu.tow_truck_bot.events.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUserLocation;

@Getter
@Setter
@Accessors(chain = true)
public class CurrentLocationProvidedEvent extends BaseEvent {

    private TelegramUserLocation telegramUserLocation;

    public CurrentLocationProvidedEvent(TelegramUser telegramUser, TelegramUserLocation telegramUserLocation) {
        super(telegramUser);
        this.telegramUserLocation = telegramUserLocation;
    }
}
