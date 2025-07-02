package ru.ser_aleu.tow_truck_bot.exceptions;

public class EventProcessingException extends RuntimeException {
    public EventProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
