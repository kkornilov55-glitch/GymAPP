package com.example.workout_api;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WorkoutController {
    private List<Exercise> databaseExercises = new ArrayList<>(List.of(
            new RepsExercise("Подтягивания", "Спина", 4, 10),
            new RepsExercise("Отжимания на брусьях", "Грудь", 4, 15),
            new TimedExercise("Планка", "Кор", 3, 60)
    ));

    @GetMapping("/workouts")
    public List<Exercise> getMyWorkouts(@RequestParam(required = false) String name) {
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

    @PostMapping("/workouts")
    public String addWorkout(@RequestBody Exercise exercise) {
        databaseExercises.add(exercise);
        return "Упражнение: " + exercise.getName() + " успешно добавлено!";
    }
}