package ru.dponyashov.kafka;

import ru.dponyashov.entity.Reception;

public interface KafkaProducer {

    void sendMail(Reception reception);

    void sendPhone(Reception reception);
}
