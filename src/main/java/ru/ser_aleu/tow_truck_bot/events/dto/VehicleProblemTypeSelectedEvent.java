package ru.ser_aleu.tow_truck_bot.events.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.VehicleProblemType;

@Getter
@Setter
@Accessors(chain = true)
public class VehicleProblemTypeSelectedEvent extends BaseEvent{

    private VehicleProblemType vehicleProblemType;

    public VehicleProblemTypeSelectedEvent(TelegramUser telegramUser, VehicleProblemType vehicleProblemType) {
        super(telegramUser);
        this.vehicleProblemType = vehicleProblemType;
    }
}
