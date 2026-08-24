package com.example.workout_api.model;

public enum WorkoutSplit {
    FULL_BODY("Всё тело"),
    UPPER_BODY("Верх тела"),
    LOWER_BODY("Низ / Ноги"),
    PUSH("Толкай: грудь, плечи, трицепс"),
    PULL("Тяни: спина, бицепс"),
    CORE("Пресс / Кор"),
    MIXED("Смешанная");


    private final String title;

    WorkoutSplit(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
