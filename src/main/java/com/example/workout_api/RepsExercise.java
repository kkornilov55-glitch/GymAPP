package com.example.workout_api;

public class RepsExercise extends Exercise implements Reportable {
    private int sets;
    private int repsPerSet;

    public RepsExercise(String name, String targetMuscleGroup, int sets, int repsPerSet) {
        super(name, targetMuscleGroup);
        this.sets = sets;
        this.repsPerSet = repsPerSet;
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
