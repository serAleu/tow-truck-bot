package ru.ser_aleu.tow_truck_bot.telegram.enums.selects;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public enum PaymentMethodType implements Selectable<PaymentMethodType> {

    CASH_OR_TRANSFER("наличные или перевод"),
    CARD("картой"),
    PAYMENT_RECEIPT("счёт на компанию");

    private final String displayName;

    @Override
    public String getDisplayName() {
        return StringUtils.isBlank(displayName) ? "не выбрано" : displayName;
    }
}