package com.example.workout_api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RepsExercise.class, name = "reps"),
        @JsonSubTypes.Type(value = TimedExercise.class, name = "timed")
})

public abstract class Exercise {
    private final String name;
    private final String targetMuscleGroup;

    public Exercise(String name, String targetMuscleGroup) {
        this.name = name;
        this.targetMuscleGroup = targetMuscleGroup;
    }

    public String getName() {
        return name;
    }

    public String getTargetMuscleGroup() {
        return targetMuscleGroup;
    }

    public abstract double calculateTotalVolume();
}
