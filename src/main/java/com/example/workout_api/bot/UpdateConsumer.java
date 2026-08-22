package com.example.workout_api.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    @Value("${admin.chatId}")
    private long adminChatId;

    private final TelegramClient client;

    public UpdateConsumer(TelegramClient client) {
        this.client = client;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            var chatId = update.getMessage().getChatId();
            var text = update.getMessage().getText();
            var userName = update.getMessage().getFrom().getFirstName();

            System.out.printf("Получено сообщение: \"%s\", от пользователя \"%s\"%n", text, chatId);
            if (text.equals("/start")) {
                sendMessage(
                        chatId,
                        String.format("Привет %s! Я бот дневник тренеровок, готов к работе!", userName));
                sendMainMenu(chatId);
            } else {
                sendMessage(chatId, "Я пока не знаю такой команды");
            }
        } else if (update.hasCallbackQuery()) {
            handleCallBackQuery(update.getCallbackQuery());
        }
    }
    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        try {
            client.execute(message);
            System.out.printf("Сообщение: \"%s\" успешно отправлено!\n", text);
        } catch (TelegramApiException e) {
            adminAlert(e, chatId);
        }
    }
    private void sendMainMenu(Long chatId) {
        SendMessage message = SendMessage.builder()
                .text("Меню:")
                .chatId(chatId)
                .build();

        var buttonBotName = InlineKeyboardButton.builder()
                .text("Как тебя зовут? ")
                .callbackData("bot_name")
                .build();
        var buttonAddExercise = InlineKeyboardButton.builder()
                .text("Добавить упражнение ")
                .callbackData("add_exercise")
                .build();

        List<InlineKeyboardRow> keyboardRows = List.of(
                new InlineKeyboardRow(buttonBotName),
                new InlineKeyboardRow(buttonAddExercise)
        );

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(keyboardRows);

        message.setReplyMarkup(markup);

        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            adminAlert(e, chatId);
        }
    }
    private void adminAlert(Exception e, Long userChatId) {
        SendMessage alert = SendMessage.builder()
                .chatId(adminChatId)
                .text(String.format("Ошибка(пользователь: %d)\n%s", userChatId, e.getMessage()))
                .build();
        try {
            client.execute(alert);
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }
    private void handleCallBackQuery(CallbackQuery callbackQuery) {
        var chatId = callbackQuery.getFrom().getId();
        var data = callbackQuery.getData();

        switch (data) {
            case "bot_name" -> sendMessage(chatId, "Меня зовут Кирилл) Будем знакомы");
            case "add_exercise" -> choiceOfExerciseType(chatId);
            default -> {
                sendMessage(chatId, "Неизвестная команда! Выберите что-то из меню");
                sendMainMenu(chatId);
            }
        }
    }
    private void choiceOfExerciseType(Long chatId) {
        SendMessage message = SendMessage.builder()
                .text("Выберите тип упражнения")
                .chatId(chatId)
                .build();

        List<KeyboardRow> keyboardRows = List.of (
                new KeyboardRow("На количество повторений"),
                new KeyboardRow("На время")
        );
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(keyboardRows);
        message.setReplyMarkup(markup);

        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            adminAlert(e, chatId);
        }
    }
}
