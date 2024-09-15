package ru.dponyashov.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.dponyashov.dto.ReceptionDto;
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
    public void sendMail(ReceptionDto reception){
        kafkaTemplate.send(mailTopicName, reception.id(),
                ClientNotify.builder()
                        .idReception(reception.id())
                        .dateOfVisit(reception.dateOfVisit().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                        .startTime(reception.startTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .nameClient(reception.client().getName())
                        .mailClient(reception.client().getMail())
                        .build());
    }
    @Override
    public void sendPhone(ReceptionDto reception){
        kafkaTemplate.send(phoneTopicName, reception.id(),
                ClientNotify.builder()
                    .idReception(reception.id())
                    .dateOfVisit(reception.dateOfVisit().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                    .startTime(reception.startTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                    .nameClient(reception.client().getName())
                    .phoneClient(reception.client().getPhone())
                .build());
    }
}
