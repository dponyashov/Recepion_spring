package ru.dponyashov.kafka;

import ru.dponyashov.dto.ReceptionDto;

public interface KafkaProducer {

    void sendMail(ReceptionDto reception);

    void sendPhone(ReceptionDto reception);
}
