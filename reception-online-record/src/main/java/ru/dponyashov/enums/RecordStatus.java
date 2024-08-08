package ru.dponyashov.enums;

public enum RecordStatus {
    NEW("NEW"),
    IN_WORK("IN_WORK"),
    CANCELLED("CANCELLED"),
    DONE("DONE");

    private final String code;

    RecordStatus(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}