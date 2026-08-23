package com.example.workout_api.model;

import com.example.workout_api.bot.BotState;

public class ExerciseDraft{
    private BotState userState = BotState.DEFAULT;
    private ExerciseType exerciseType;
    private String name;
    private String targetMuscleGroup;
    private Integer sets, duration, reps;

    public BotState getUserState() {
        return userState;
    }

    public void setUserState(BotState userState) {
        this.userState = userState;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetMuscleGroup() {
        return targetMuscleGroup;
    }

    public void setTargetMuscleGroup(String targetMuscleGroup) {
        this.targetMuscleGroup = targetMuscleGroup;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
