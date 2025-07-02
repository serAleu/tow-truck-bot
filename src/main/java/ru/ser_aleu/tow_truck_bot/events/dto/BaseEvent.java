package ru.ser_aleu.tow_truck_bot.events.dto;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
public abstract class BaseEvent {
    private Long chatId;
}