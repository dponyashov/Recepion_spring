package ru.dponyashov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import ru.dponyashov.service.NotificationService;

@Service
@Log4j2
@RequiredArgsConstructor
public class MailNotificationService implements NotificationService {

    private final MailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${reception.subject}")
    private String subject;

    @Override
    public void sendMessage(String messageText, String to) {

        if(to.isEmpty()){
            log.info("Сообщение {} не отправлено, т.к. адрес не указан", messageText);
            return;
        }
        try {
            sendSimpleEmail(to, subject, messageText);
            log.info("Отправлено сообщение '{}' на почту '{}'", messageText, to);
        } catch (MailException mailException){
            log.info("Ошибка отправки почты '{}'", mailException.getMessage());
        } catch (Exception ex){
            log.info("Неизвестная ошибка при отправке почты: '{}'", ex.getMessage());
        }
    }

    private void sendSimpleEmail(String toAddress, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailFrom);
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
    }
}
