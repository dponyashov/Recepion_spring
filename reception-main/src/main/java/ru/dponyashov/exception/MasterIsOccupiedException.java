package ru.dponyashov.exception;

import ru.dponyashov.entity.Reception;

import java.time.format.DateTimeFormatter;

public class MasterIsOccupiedException extends RuntimeException{
    public MasterIsOccupiedException(Reception reception) {
        super(String.format("Мастер занят на %s в интервале с %s по %s",
                reception.getDateOfVisit().format(DateTimeFormatter.ISO_DATE),
                reception.getStartTime().format(DateTimeFormatter.ISO_TIME),
                reception.getFinishTime().format(DateTimeFormatter.ISO_TIME)));
    }
}
