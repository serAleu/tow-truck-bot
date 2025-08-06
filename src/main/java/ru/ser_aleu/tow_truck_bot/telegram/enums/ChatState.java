package ru.ser_aleu.tow_truck_bot.telegram.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChatState {
    START(false),
    PUT_TEXT(false),
    AWAITING_VEHICLE_TYPE_SELECTION(false),
    AWAITING_VEHICLE_TYPE_PROBLEM_SELECTION(false),
    AWAITING_CURRENT_LOCATION_PROVIDING(false),
    AWAITING_DESTINATION_LOCATION_PROVIDING(false),
    LOCATION_RECEIVED(false),
    CONFIRMATION(false),
    WAITING_DRIVER(true);

    private final Boolean isFinalState;
}
