package com.example.workout_api;

public abstract class Exercise {
    private String name;
    private String targetMuscleGroup;

    public Exercise() {};

    public Exercise(String name, String targetMuscleGroup) {
        this.name = name;
        this.targetMuscleGroup = targetMuscleGroup;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTargetMuscleGroup(String targetMuscleGroup) {
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
