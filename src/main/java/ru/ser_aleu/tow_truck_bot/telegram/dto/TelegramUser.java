package ru.ser_aleu.tow_truck_bot.telegram.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.VehicleType;
import ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.Selectable;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class TelegramUser {
    private Long chatId;
    private String userName;
    private String telegramUserName;
    private String phoneNumber;
    private Update update;
    private TelegramUserLocation userLocation;
    private Map<ChatState, Selectable> userSelections;
    private ChatState currentChatState;
    private VehicleType vehicleType;
}
