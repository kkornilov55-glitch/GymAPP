package com.example.workout_api.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient client;

    public UpdateConsumer(TelegramClient client) {
        this.client = client;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            System.out.printf("Получено сообщение: \"%s\", от пользователя \"%s\"%n", text, chatId);
            SendMessage(chatId, "Привет! Я бот дневник тренеровок, готов к работе!");
        }
    }
    private void SendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        message.setChatId(chatId);
        message.setText(text);

        try {
            client.execute(message);
            System.out.println("Сообщение успешно отправлено!");
        } catch (TelegramApiException e) {
            System.out.printf("Не удалось отправить сообщение: \"%s\"\n", text);
        }
    }
}
