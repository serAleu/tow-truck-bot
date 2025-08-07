package ru.ser_aleu.tow_truck_bot.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class TelegramUserLocation {
    private Double latitude;
    private Double longitude;

    @Override
    public String toString() {
        return "ширина: " + latitude + ", долгота: " + longitude;
    }
}
