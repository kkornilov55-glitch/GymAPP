package com.example.workout_api.model;

public enum WorkoutType {
    STRENGTH("Силовая (с железом)"),
    CALISTHENICS("Воркаут (свой вес)"),
    CARDIO("Кардио"),
    STRETCHING("Растяжка"),
    CUSTOM("Другое");


    private final String title;

    WorkoutType(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}

