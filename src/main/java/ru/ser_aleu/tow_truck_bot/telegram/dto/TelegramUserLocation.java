package ru.ser_aleu.tow_truck_bot.telegram.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class TelegramUserLocation {
    private ChatState currentState = ChatState.START;
    private Double latitude;
    private Double longitude;
    private String carType;
}
