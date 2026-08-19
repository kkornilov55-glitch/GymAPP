package com.example.workout_api;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/workouts")
    public List<Exercise> getMyWorkouts(@RequestParam(required = false) String name) {
        return workoutService.getWorkouts(name);
    }

    @PostMapping("/workouts")
    public String addWorkout(@RequestBody Exercise exercise) {
        workoutService.addWorkout(exercise);
        return "Упражнение: " + exercise.getName() + " успешно добавлено!";
    }
}