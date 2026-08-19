package com.example.workout_api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RepsExercise extends Exercise implements Reportable {
    private final int sets;
    private final int repsPerSet;

    @JsonCreator
    public RepsExercise(@JsonProperty("name") String name,
                        @JsonProperty("targetMuscleGroup") String targetMuscleGroup,
                        @JsonProperty("sets") int sets,
                        @JsonProperty("repsPerSet") int repsPerSet) {
        super(name, targetMuscleGroup);
        this.sets = sets;
        this.repsPerSet = repsPerSet;
    }

    public int getSets() {
        return sets;
    }

    public int getRepsPerSet() {
        return repsPerSet;
    }

    @Override
    public double calculateTotalVolume() {
        return sets * repsPerSet;
    }

    @Override
    public void printSummary() {
        System.out.printf("[%s] %s — %d x %d \n", getTargetMuscleGroup(), getName(), repsPerSet, sets);
    }
}
