package ru.ser_aleu.tow_truck_bot.telegram.enums.selects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VehicleProblemType implements Selectable<VehicleProblemType> {

    NO_PROBLEMS("новый или с пробегом, заводится и едет, неисправностей нет"),
    HAS_ISSUES("заводится и едет, но есть неисправность"),
    WONT_START_CAN_NEUTRAL("не заводится, можно перевести КПП в нейтраль и снять ручной тормоз"),
    WONT_START_CANT_NEUTRAL("не заводится, нельзя перевести в нейтраль и снять ручной тормоз"),
    DAMAGED_IN_ACCIDENT("повреждён в ДТП");

    private final String displayName;

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
