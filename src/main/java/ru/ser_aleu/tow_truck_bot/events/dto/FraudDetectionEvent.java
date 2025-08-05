package ru.ser_aleu.tow_truck_bot.events.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;

@Getter
@Setter
@Accessors(chain = true)
public class FraudDetectionEvent extends BaseEvent {

    public FraudDetectionEvent(TelegramUser telegramUser) {
        super(telegramUser);
    }
}
