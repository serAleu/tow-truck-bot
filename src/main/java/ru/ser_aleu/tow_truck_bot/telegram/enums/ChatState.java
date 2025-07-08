package ru.ser_aleu.tow_truck_bot.telegram.enums;

public enum ChatState {
    START,
    PUT_TEXT,
    AWAITING_LOCATION,
    LOCATION_RECEIVED,
    AWAITING_CAR_TYPE,
    AWAITING_PROBLEM_DESCRIPTION,
    CONFIRMATION,
    WAITING_DRIVER
}
