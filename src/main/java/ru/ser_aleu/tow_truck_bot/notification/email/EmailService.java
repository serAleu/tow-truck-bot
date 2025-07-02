package ru.ser_aleu.tow_truck_bot.notification.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    public void send(String mail, String eventServiceAlert, String message) {
        log.info("Email was sent. mail = {}, eventServiceAlert = {}, message = {}", mail, eventServiceAlert, message);
    }
}
