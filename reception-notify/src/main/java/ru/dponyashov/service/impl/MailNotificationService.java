package ru.dponyashov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.dponyashov.service.NotificationService;

@Service
@Log4j2
@RequiredArgsConstructor
public class MailNotificationService implements NotificationService {

    private final JavaMailSender javaMailSender;

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

//    @Override
    public void sendSimpleEmail(String toAddress, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }
}
