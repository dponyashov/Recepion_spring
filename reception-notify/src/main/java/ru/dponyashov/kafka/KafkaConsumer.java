package ru.dponyashov.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.dponyashov.kafkadto.ClientNotify;
import ru.dponyashov.service.NotificationService;
import ru.dponyashov.utils.MessageMaker;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final NotificationService mailNotificationService;
    private final NotificationService phoneNotificationService;
    private final MessageMaker messageMaker;

    @KafkaListener(topics = "${kafka.topics.mail}", groupId = "reception")
    public void mailTopicListener(ConsumerRecord<Long, ClientNotify> record){
        String message = messageMaker.makeMessageFromData(record.value());
        mailNotificationService.sendMessage(message);
    }

    @KafkaListener(topics = "${kafka.topics.phone}", groupId = "reception")
    public void phoneTopicListener(ConsumerRecord<Long, ClientNotify> record){
        String message = messageMaker.makeMessageFromData(record.value());
        phoneNotificationService.sendMessage(message);
    }
}