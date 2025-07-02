package ru.ser_aleu.tow_truck_bot.notification;

public interface NotificationService {
    void sendAlert(String message);
    void sendWarning(String message);
}
