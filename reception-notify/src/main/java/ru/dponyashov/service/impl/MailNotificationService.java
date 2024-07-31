package ru.dponyashov.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dponyashov.service.NotificationService;

@Service
@Log4j2
public class MailNotificationService implements NotificationService {
    @Override
    public void sendMessage(String message) {
        String textMessage = "mail: " + message;

        log.info(textMessage);
    }
}
