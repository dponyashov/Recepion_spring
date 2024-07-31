package ru.dponyashov.exception;

import ru.dponyashov.entity.Reception;

import java.time.format.DateTimeFormatter;

public class RoomIsOccupiedException extends RuntimeException{
    public RoomIsOccupiedException(Reception reception) {
        super(String.format("Комната занята на %s в указанном интервале с %s по %s",
                reception.getDateOfVisit().format(DateTimeFormatter.ISO_DATE),
                reception.getStartTime().format(DateTimeFormatter.ISO_TIME),
                reception.getFinishTime().format(DateTimeFormatter.ISO_TIME)));
    }
}