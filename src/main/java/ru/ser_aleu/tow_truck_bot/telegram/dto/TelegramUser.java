package ru.ser_aleu.tow_truck_bot.telegram.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.*;
import ru.ser_aleu.tow_truck_bot.telegram.enums.ChatState;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class TelegramUser {
    private Long chatId;
    private String userName;
    private String telegramUserName;
    private String phoneNumber;
    private ChatState currentChatState;
    private VehicleType vehicleType;
    private VehicleProblemType vehicleProblemType;
    private PaymentMethodType paymentMethodType;
    private TelegramUserLocation currentLocation;
    private TelegramUserLocation destinationLocation;
    private YesOrNo needDocs;
    private Boolean orderConfirmed;
    private Update update;

    @Override
    public String toString() {
        return "Заказ: " + '\n' +
                "Имя пользователя = " + telegramUserName + '\n' +
                "Номер телефона = " + phoneNumber + '\n' +
                "Тип транспортного средства = " + (vehicleType != null ? vehicleType.getDisplayName() : "не выбрано") + '\n' +
                "Тип проблемы = " + (vehicleProblemType != null ? vehicleProblemType.getDisplayName() : "не выбрано") + '\n' +
                "Выбранный способ оплаты = " + (paymentMethodType != null ? paymentMethodType.getDisplayName() : "не выбрано")+ '\n' +
                "Текущая локация транспортного средства = " + currentLocation + '\n' +
                "Локация куда необходимо доставить транспортное средство = " + destinationLocation + '\n' +
                "Нужен ли чек/акт = " + (needDocs != null ? needDocs.getDisplayName() : "не выбрано") + '\n' +
                "Сумма = не посчитано";
    }
}
