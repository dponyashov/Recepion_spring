package ru.dponyashov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.dponyashov.dto.RecordDto;
import ru.dponyashov.enums.BotCommand;
import ru.dponyashov.enums.CommandStatus;

@Service
@RequiredArgsConstructor
public class BotService {

    private final RecordService recordService;

    private CommandStatus commandStatus = CommandStatus.NONE;
    private final RecordDto recordDto = new RecordDto();

    public SendMessage processCommand(Update update) {
        var text = update.getMessage().getText();
        BotCommand command = BotCommand.forNameIgnoreCase(text);
        if(command == null){
            return commandProcess(update, text);
        }

        switch (command){
            case START -> {
                return startProcess(update);
            }
            case CANCEL -> {
                clearDto();
                return cancelProcess(update);
            }
            case RECORD -> {
                return recordProcess(update);
            }
            case SAVE -> {
                return saveProcess(update);
            }
            default -> {
                return helpProcess(update);
            }
        }
    }

    private SendMessage saveProcess(Update update) {
        if (commandStatus == CommandStatus.SAVE) {

            try {
                recordService.save(recordDto);
            } catch(RuntimeException exception){
                commandStatus = CommandStatus.NONE;
                recordDto.clear();
                String text = String.format("Сохранить данные не удалось из-за ошибки:\n%s\n/record - чтобы начать ввод",
                        exception.getMessage());
                return generateSendMessageWithText(update, text);
            }

            commandStatus = CommandStatus.NONE;
            String text = "Данные сохранены, спасибо за обращение.\n" +
                    recordDto.getPhone() + " - " + recordDto.getName() + " - " + recordDto.getNote();
            return generateSendMessageWithText(update, text);
        } else {
            String text = "Необходимо ввести все данные, прежде чем сохранять\n/help - для просмотра команд";
            return generateSendMessageWithText(update, text);
        }
    }

    private SendMessage cancelProcess(Update update) {
        if(commandStatus == CommandStatus.NONE){
            return generateSendMessageWithText(update,
                    "Данные не вводились или были сохранены\n/record - чтобы начать ввод");
        }
        commandStatus = CommandStatus.NONE;
        recordDto.clear();
        return generateSendMessageWithText(update,
                "Предыдущий ввод отменен\n/record - чтобы начать ввод");
    }

    private SendMessage helpProcess(Update update) {
        StringBuilder sb = new StringBuilder("Поддерживаемые команды:\n");
        sb.append("/start - старт работы\n");
        sb.append("/help - список команд\n");
        sb.append("/record - ввод данных для записи\n");
        sb.append("/save - сохранить введенные записи\n");
        sb.append("/cancel - очистка введенных данных");

        return generateSendMessageWithText(update, sb.toString());
    }

    private SendMessage startProcess(Update update) {
        return generateSendMessageWithText(update,
                "Добро пожаловать в онлайн запись\n/help - для получения списка команд");
    }

    private SendMessage recordProcess(Update update) {
        if(commandStatus == CommandStatus.SAVE){
            return generateSendMessageWithText(update,
                    "Данные введены.\n/save - для сохранения\n/cancel - для отмены");
        }
        commandStatus = CommandStatus.PHONE;
        return generateSendMessageWithText(update,
                "Выполняем команду записи\nВведите телефон для связи");
    }

    private SendMessage commandProcess(Update update, String text) {
        if(text.startsWith("/")){
            return unsupportedCommand(update);
        }
        switch (commandStatus){
            case PHONE -> {
                if(text.isEmpty()){
                    return generateSendMessageWithText(update,
                            "Телефон не может быть пустым\nВведите номер телефона для связи");
                } else {
                    recordDto.setPhone(text);
                    commandStatus = CommandStatus.NAME;
                    return generateSendMessageWithText(update,
                            "Введите имя");
                }
            }
            case NAME -> {
                if(text.isEmpty()){
                    var sendMessage = generateSendMessageWithText(update,
                            "Имя не может быть пустым\nВведите имя");
                } else {
                    recordDto.setName(text);
                    commandStatus = CommandStatus.NOTE;
                    return generateSendMessageWithText(update,
                            "Введите дополнительную информацию");
                }
            }
            case NOTE -> {
                recordDto.setNote(text);
                commandStatus = CommandStatus.SAVE;
                return generateSendMessageWithText(update,
                        "Данные введены\n/save - для сохранения\n/cancel - для отмены");
            }
            case SAVE -> {
                return generateSendMessageWithText(update,
                        "Для сохранения информации выполните команду /save\nДля отмены команду /cancel");
            }
        }
        return unsupportedCommand(update);
    }

    private void clearDto() {
        recordDto.clear();
    }

    public SendMessage unsupportedCommand(Update update) {
        return generateSendMessageWithText(update,
                "Не поддерживаемый контент\n/help - для получения списка команд");
    }

    public SendMessage generateSendMessageWithText(Update update, String text){
        return new SendMessage(update.getMessage().getChatId().toString(), text);
    }
}
