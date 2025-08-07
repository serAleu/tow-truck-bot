package ru.ser_aleu.tow_truck_bot.telegram.enums.selects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public enum VehicleType implements Selectable<VehicleType> {
    MOTORBIKE("мотоцикл"),
    PASSENGER_CAR("легковой автомобиль"),
    CROSSOVER("кроссовер"),
    SUV_OR_MINIBUS("внедорожник/джип/микроавтобус"),
    PREMIUM_OR_SPORTS_CAR("автомобиль премиум класса или спорткар"),
    COMMERCIAL_VEHICLE("коммерческий транспорт"),
    SPECIAL_EQUIPMENT("спецтехника");

    private final String displayName;

    @Override
    public String getDisplayName() {
        return StringUtils.isBlank(displayName) ? "не выбрано" : displayName;
    }
}
