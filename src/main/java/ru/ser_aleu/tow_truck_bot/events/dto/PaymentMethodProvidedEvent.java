package ru.ser_aleu.tow_truck_bot.events.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.ser_aleu.tow_truck_bot.telegram.dto.TelegramUser;
import ru.ser_aleu.tow_truck_bot.telegram.enums.selects.PaymentMethodType;

@Getter
@Setter
@Accessors(chain = true)
public class PaymentMethodProvidedEvent extends BaseEvent {

    private PaymentMethodType paymentMethodType;

    public PaymentMethodProvidedEvent(TelegramUser telegramUser, PaymentMethodType paymentMethodType) {
        super(telegramUser);
        this.paymentMethodType = paymentMethodType;
    }
}