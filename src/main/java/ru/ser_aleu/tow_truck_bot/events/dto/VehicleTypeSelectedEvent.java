package ru.ser_aleu.tow_truck_bot.events.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.VehicleType;

@Getter
@Setter
@Accessors(chain = true)
public class VehicleTypeSelectedEvent extends BaseEvent {

    private VehicleType vehicleType;

    public VehicleTypeSelectedEvent(TelegramUser telegramUser) {
        super(telegramUser);
    }
}
