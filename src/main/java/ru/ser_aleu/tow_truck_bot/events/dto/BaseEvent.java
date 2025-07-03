package ru.ser_aleu.tow_truck_bot.events.dto;

import lombok.Getter;
import lombok.experimental.Accessors;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;

@Getter
@Accessors(chain = true)
public abstract class BaseEvent {
    private TelegramUser telegramUser;
}