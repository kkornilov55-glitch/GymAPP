package com.example.workout_api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WorkoutController {
    private final List<Exercise> databaseExercises = new ArrayList<>(List.of(
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
}