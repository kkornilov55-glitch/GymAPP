package com.example.workout_api.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Service
public class MessageSender {

    private final TelegramClient client;

    @Value("${admin.chatId}")
    private long adminChatId;

    public MessageSender(TelegramClient client) {
        this.client = client;
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        try {
            client.execute(message);
            System.out.printf("Сообщение: \"%s\" успешно отправлено!\n", text);
        } catch (TelegramApiException e) {
            adminAlert(e, chatId);
        }
    }
    public void sendMessageAndRemoveKeyboard(Long chatId, String text) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        ReplyKeyboardRemove keyboardRemove = ReplyKeyboardRemove.builder()
                .removeKeyboard(true)
                .build();
        message.setReplyMarkup(keyboardRemove);
        try {
            client.execute(message);
            System.out.printf("Сообщение: \"%s\" успешно отправлено!\n", text);
        } catch (TelegramApiException e) {
            adminAlert(e, chatId);
        }
    }
    public void sendMainMenu(Long chatId) {
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
        var buttonPrintExercisesList = InlineKeyboardButton.builder()
                .text("Вывести список упражнений")
                .callbackData("exercises_list")
                .build();

        List<InlineKeyboardRow> keyboardRows = List.of(
                new InlineKeyboardRow(buttonBotName),
                new InlineKeyboardRow(buttonAddExercise),
                new InlineKeyboardRow(buttonPrintExercisesList)
        );

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(keyboardRows);

        message.setReplyMarkup(markup);

        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            adminAlert(e, chatId);
        }
    }
    public void adminAlert(Exception e, Long userChatId) {
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
    public void sendChoiceExerciseTypeMenu(Long chatId) {
        SendMessage message = SendMessage.builder()
                .text("Выберите тип упражнения")
                .chatId(chatId)
                .build();

        List<KeyboardRow> keyboardRows = List.of (
                new KeyboardRow("На повторы"),
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
