package ru.dponyashov.utils;

import ru.dponyashov.kafkadto.ClientNotify;

public interface MessageMaker {

    String makeMessageFromData(ClientNotify clientData);

    String makeShortMessageFromData(ClientNotify clientData);
}