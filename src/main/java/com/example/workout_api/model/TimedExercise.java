package com.example.workout_api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimedExercise extends Exercise implements Reportable {

    private final int durationSeconds;
    private final int sets;

    @JsonCreator
    public TimedExercise(@JsonProperty("name") String name,
                         @JsonProperty("targetMuscleGroup") String targetMuscleGroup,
                         @JsonProperty("sets") int sets,
                         @JsonProperty("durationSeconds") int durationSeconds) {
        super(name, targetMuscleGroup);
        this.sets = sets;
        this.durationSeconds = durationSeconds;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public int getSets() {
        return sets;
    }

    @Override
    public double calculateTotalVolume() {
        return durationSeconds * sets;
    }
    @Override
    public String getSummary() {
        return String.format("[%s] %s — %d сек х %d \n", getTargetMuscleGroup(), getName(), durationSeconds, sets);
    }


}
