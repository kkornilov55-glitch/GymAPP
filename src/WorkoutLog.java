import java.time.LocalDate;
import java.util.ArrayList;

public class WorkoutLog {
    private ArrayList<Exercise> exercises = new ArrayList<>();
    private LocalDate date;

    public void addExercise(Exercise e) {
        exercises.add(e);
    }

    public void printReport() {
        for (Exercise e : exercises) {
            if (e instanceof Reportable rep)
                rep.printSummary();
        }
    }
}
