package ru.dponyashov.enums;

public enum CommandStatus {
    NONE("none"),
    PHONE("phone"),
    NAME("name"),
    NOTE("note"),
    SAVE("save");

    private final String value;

    public String value(){
        return value;
    }

    CommandStatus(String value) {
        this.value = value;
    }
}