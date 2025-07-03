package ru.ser_aleu.tow_truck_bot.telegram.enums.selects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum CarType implements Selectable {
    CAR_TYPE1("легковой"),
    CAR_TYPE2("грузовой"),
    CAR_TYPE3("двухколесный");

    private final String displayName;
}
