package ru.dponyashov.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dponyashov.service.BotService;

@Log4j2
@Service
public class BotController extends TelegramLongPollingBot {

    private final BotService borService;

    @Value("${bot.name}")
    private String botName;

    public BotController(@Value("${bot.token}") String botToken, BotService borService){
        super(botToken);
        this.borService = borService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update == null ){
            log.info("Полученный update пустой");
            return;
        }

        if(update.hasMessage()){
            messageDispatcher(update);
        } else {
            log.info("Полученный update не содержит сообщения");
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private void messageDispatcher(Update update) {
        var message = update.getMessage();
        if(message.hasText()){
            processText(update);
        } else {
            processUnsupported(update);
        }
    }

    private void processText(Update update) {
        var sendMessage = borService.processCommand(update);
        setView(sendMessage);
    }

    private void processUnsupported(Update update) {
        var sendMessage = borService.unsupportedCommand(update);
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        if(sendMessage == null){
            return;
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
