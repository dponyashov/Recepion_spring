package ru.dponyashov.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.dponyashov.entity.Reception;
import ru.dponyashov.kafkadto.ClientNotify;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SimpleKafkaProducer implements KafkaProducer {

    @Value("${kafka.topics.mail}")
    private String mailTopicName;

    @Value("${kafka.topics.phone}")
    private String phoneTopicName;

    private final KafkaTemplate<Long, ClientNotify> kafkaTemplate;

    @Override
    public void sendMail(Reception reception){
        kafkaTemplate.send(mailTopicName, reception.getId(),
                ClientNotify.builder()
                        .idReception(reception.getId())
                        .dateOfVisit(reception.getDateOfVisit().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                        .startTime(reception.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .nameClient(reception.getClient().getName())
                        .mailClient(reception.getClient().getMail())
                        .build());
    }
    @Override
    public void sendPhone(Reception reception){
        kafkaTemplate.send(phoneTopicName, reception.getId(),
                ClientNotify.builder()
                    .idReception(reception.getId())
                    .dateOfVisit(reception.getDateOfVisit().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                    .startTime(reception.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                    .nameClient(reception.getClient().getName())
                    .phoneClient(reception.getClient().getPhone())
                .build());
    }
}
