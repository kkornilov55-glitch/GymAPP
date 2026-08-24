package com.example.workout_api.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Workout {
    private final LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private final WorkoutType workoutType;
    private final WorkoutSplit workoutSplit;
    private List<Exercise> ExercisesList;

    public Workout(WorkoutType type, WorkoutSplit split) {
        workoutType = type;
        workoutSplit = split;
        date = LocalDate.now();
    }

    public void addExercise(Exercise e) {
        if (ExercisesList == null)
            ExercisesList = new ArrayList<>();

        ExercisesList.add(e);
    }

    public void startWorkout() {
        startTime = LocalTime.now();
    }

    public void endWorkout() {
        endTime = LocalTime.now();
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Exercise> getExercisesList() {
        return ExercisesList;
    }

    public WorkoutType getWorkoutType() {
        return workoutType;
    }

    public WorkoutSplit getWorkoutSplit() {
        return workoutSplit;
    }
}
