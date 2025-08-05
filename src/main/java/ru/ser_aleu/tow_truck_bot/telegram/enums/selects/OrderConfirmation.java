package ru.ser_aleu.tow_truck_bot.telegram.enums.selects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderConfirmation implements Selectable<OrderConfirmation> {
    CONFIRMED("Заказ принят"),
    REJECTED("Заказ отклонен");

    private final String displayName;
}
