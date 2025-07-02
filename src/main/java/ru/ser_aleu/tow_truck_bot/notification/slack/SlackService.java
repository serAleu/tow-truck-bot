package ru.ser_aleu.tow_truck_bot.notification.slack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SlackService {
    public void sendToChannel(String hashtag, String message) {
        log.info("Message was sent to channel. hashtag = {}, message = {}", hashtag, message);
    }
}
