package com.example.workout_api;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutService {
    private List<Exercise> databaseExercises = new ArrayList<>(List.of(
            new RepsExercise("Подтягивания", "Спина", 4, 10),
            new RepsExercise("Отжимания на брусьях", "Грудь", 4, 15),
            new TimedExercise("Планка", "Кор", 3, 60)
    ));

    public List<Exercise> getWorkouts(String name) {
        if (name == null)
            return databaseExercises;
        else {
            List<Exercise> filteredExercises = new ArrayList<>();
            for (Exercise e : databaseExercises) {
                if (e.getName().equalsIgnoreCase(name)) {
                    filteredExercises.add(e);
                    break;
                }
            }
            return filteredExercises;
        }
    }

    public void addWorkout(Exercise exercise) {
        databaseExercises.add(exercise);
    }
}
