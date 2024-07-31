package ru.dponyashov.utils.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.dponyashov.kafkadto.ClientNotify;
import ru.dponyashov.utils.MessageMaker;

@Component
public class SimpleMessageMaker implements MessageMaker {

    @Value("${reception.name}")
    private String receptionName;

    @Override
    public String makeMessageFromData(ClientNotify clientData) {
        StringBuilder message = new StringBuilder("Уважаемый ");
        message.append(clientData.getNameClient());
        message.append(", вы записаны на процедуру в ");
        message.append(receptionName);
        message.append(" на ");
        message.append(clientData.getDateOfVisit());
        message.append(" в ");
        message.append(clientData.getStartTime());
        return message.toString();
    }

    @Override
    public String makeShortMessageFromData(ClientNotify clientData) {
        StringBuilder message = new StringBuilder("Запись в ");
        message.append(clientData.getNameClient());
        message.append(" на ");
        message.append(clientData.getDateOfVisit());
        return message.toString();
    }
}
