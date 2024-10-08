package ru.dponyashov.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dponyashov.service.NotificationService;

@Service
@Log4j2
public class PhoneNotificationService implements NotificationService {
    @Override
    public void sendMessage(String messageText, String to) {

        //TODO реализация смс уведомления

        log.info("Отправлено сообщение '{}' на телефон '{}'", messageText, to);
    }
}