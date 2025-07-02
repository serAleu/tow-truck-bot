package ru.ser_aleu.tow_truck_bot.telegram.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.ser_aleu.tow_truck_bot.telegram.enums.CarType;
import ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class TelegramUser {
    private Long chatId;
    private String userName;
    private String phoneNumber;
    private TelegramUserLocation userLocation;
    private Map<ChatState, String> communications;
    private ChatState chatState;
    private CarType carType;
}
