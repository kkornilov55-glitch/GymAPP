package com.example.workout_api.bot;

import com.example.workout_api.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    @Value("${admin.chatId}")
    private long adminChatId;

    private final TelegramClient client;

    public UpdateConsumer(TelegramClient client) {
        this.client = client;
    }

    private Map<Long, ExerciseDraft> userExerciseDrafts = new ConcurrentHashMap<>();

    private Map<Long, List<Exercise>> userExercises = new ConcurrentHashMap<>();

    private List<Exercise> getUserExercises(Long chatId) {
        return userExercises.computeIfAbsent(chatId, k -> new ArrayList<>());
    }

    private ExerciseDraft getUserExerciseDraft(Long chatId) {
        if (!userExerciseDrafts.containsKey(chatId))
            userExerciseDrafts.put(chatId, new ExerciseDraft());
        return userExerciseDrafts.get(chatId);
    }

    private BotState getUserState(Long chatId) {
        return getUserExerciseDraft(chatId).getUserState();
    }
    private void setUserState(Long chatId, BotState state) {
        ExerciseDraft draft = getUserExerciseDraft(chatId);
        draft.setUserState(state);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            var chatId = update.getMessage().getChatId();
            var text = update.getMessage().getText();
            var userName = update.getMessage().getFrom().getFirstName();

            System.out.printf("Получено сообщение: \"%s\", от пользователя \"%s\"%n", text, chatId);

            if (text.equals("/menu") || text.equals("/start")) {
                if (text.equals("/start"))
                    sendMessage(
                            chatId,
                            String.format("Привет %s! Я бот дневник тренировок, готов к работе!", userName));

                sendMainMenu(chatId);
                setUserState(chatId, BotState.DEFAULT);
                return;
            }

            var draft = getUserExerciseDraft(chatId);
            var state = getUserState(chatId);
            switch (state) {
                case DEFAULT -> {
                    sendMessage(chatId, "Я не знаю такой команды");
                    sendMainMenu(chatId);
                }
                case WAITING_FOR_EXERCISE_TYPE -> handleExerciseType(chatId, draft, text);
                case WAITING_FOR_EXERCISE_NAME -> handleExerciseName(chatId, draft, text);
                case WAITING_FOR_TARGET_MUSCLE_GROUP -> handleMuscleGroup(chatId, draft, text);
                case WAITING_FOR_SETS -> handleExerciseSets(chatId, draft, text);
                case WAITING_FOR_REPS -> handleExerciseReps(chatId, draft, text);
                case WAITING_FOR_TIME -> handleExerciseTime(chatId, draft, text);
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
            case "exercises_list" -> printExercisesList(chatId);
            default -> {
                sendMessage(chatId, "Неизвестная команда! Выберите что-то из меню");
                sendMainMenu(chatId);
            }
        }
    }

    private void choiceOfExerciseType(Long chatId) {

        setUserState(chatId, BotState.WAITING_FOR_EXERCISE_TYPE);

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
    private void sendMessageAndRemoveKeyboard(Long chatId, String text) {
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

    private void printExercisesList(Long chatId) {
        var exercises = getUserExercises(chatId);
        if (exercises.isEmpty()) {
            sendMessage(chatId, "Для начала необходимо добавить хотя бы 1 упражнение!");
            return;
        }

        StringBuilder messageBuilder = new StringBuilder("Ваш список упражнений:\n\n");
        for (Exercise e : exercises) {
            if (e instanceof Reportable rep)
                messageBuilder.append(rep.getSummary());
            else
                messageBuilder.append(e.getName() + "\n");
        }
        sendMessage(chatId, messageBuilder.toString());
    }

    private void handleExerciseType(Long chatId, ExerciseDraft draft, String textType) {
        if (textType.equals("На повторы"))
            draft.setExerciseType(ExerciseType.REPS);
        else if (textType.equals("На время"))
            draft.setExerciseType(ExerciseType.TIMED);
        else {
            sendMessage(chatId, "Пожалуйста, выберите тип упражнения при помощи клавиатуры! Попробуйте снова:");
            return;
        }

        sendMessageAndRemoveKeyboard(chatId, "Отлично! Напишите название упражнения:");
        setUserState(chatId, BotState.WAITING_FOR_EXERCISE_NAME);
    }
    private void handleExerciseName(Long chatId, ExerciseDraft draft, String name) {
        draft.setName(name.trim());

        sendMessage(chatId, "На какую группу(ы) мышц это упражнение (одним сообщением):");
        setUserState(chatId, BotState.WAITING_FOR_TARGET_MUSCLE_GROUP);
    }
    private void handleMuscleGroup(Long chatId, ExerciseDraft draft, String targetMuscleGroup) {
        draft.setTargetMuscleGroup(targetMuscleGroup.trim());

        sendMessage(chatId, "Количество подходов:");
        setUserState(chatId, BotState.WAITING_FOR_SETS);
    }
    private void handleExerciseSets(Long chatId, ExerciseDraft draft, String sets) {
        try {
            int convertedSets = Integer.parseInt(sets);

            draft.setSets(convertedSets);

            switch (draft.getExerciseType()) {
                case REPS -> {
                    sendMessage(chatId, "Количество повторений в подходе:");
                    setUserState(chatId, BotState.WAITING_FOR_REPS);
                }
                case TIMED -> {
                    sendMessage(chatId, "Время выполнения упражнения (сек):");
                    setUserState(chatId, BotState.WAITING_FOR_TIME);
                }
            }
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Пожалуйста, введите количество подходов целым числом! Попробуйте снова:");
        }
    }
    private void handleExerciseReps(Long chatId, ExerciseDraft draft, String reps) {
        try {
            int convertedReps = Integer.parseInt(reps);
            draft.setReps(convertedReps);

            var exercise = convertToExercise(draft);
            saveExercise(chatId, exercise);

            sendMainMenu(chatId);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Пожалуйста, введите количество повторений целым числом! Попробуйте снова:");
        }
    }
    private void handleExerciseTime(Long chatId, ExerciseDraft draft, String time) {
        try {
            int convertedTime = Integer.parseInt(time);
            draft.setDuration(convertedTime);

            var exercise = convertToExercise(draft);
            saveExercise(chatId, exercise);

            sendMainMenu(chatId);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Пожалуйста, введите время выполнения целым числом! Попробуйте снова:");
        }
    }
    private Exercise convertToExercise(ExerciseDraft draft) {
        ExerciseType type = draft.getExerciseType();
        Exercise exercise = null;
        switch (type) {
            case REPS -> exercise = new RepsExercise(
                    draft.getName(),
                    draft.getTargetMuscleGroup(),
                    draft.getSets(),
                    draft.getReps());
            case TIMED -> exercise = new TimedExercise(
                    draft.getName(),
                    draft.getTargetMuscleGroup(),
                    draft.getSets(),
                    draft.getDuration());
        }
        return exercise;
    }
    private void saveExercise(Long chatId, Exercise exercise) {
        var exercises = getUserExercises(chatId);
        exercises.add(exercise);
        userExerciseDrafts.remove(chatId);

        sendMessage(chatId, "Упражнение добавлено!");
        setUserState(chatId, BotState.DEFAULT);
    }
}
