package ru.dponyashov.enums;

public enum BotCommand {
    START("/start"),
    HELP("/help"),
    RECORD("/record"),
    SAVE("/save"),
    CANCEL("/cancel");

    private final String value;

    public String value(){
        return this.value;
    }

    BotCommand(String value) {
        this.value = value;
    }

    static public BotCommand forNameIgnoreCase(String value) {
        for (BotCommand command : BotCommand.values()) {
            if ( command.value().equalsIgnoreCase(value) ) {
                return command;
            }
        }
        return null;
    }
}
