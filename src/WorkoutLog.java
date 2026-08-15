import java.time.LocalDate;
import java.util.ArrayList;

public class WorkoutLog {
    private ArrayList<Exercise> exercises = new ArrayList<>();
    private LocalDate date;

    public void addExercise(Exercise e) {
        exercises.add(e);
    }

    public void printReport() {
        if (exercises.size() < 1) return;

        date = LocalDate.now();
        System.out.printf("\nТренеровка за %s\n---\n", date);
        for (Exercise e : exercises) {
            if (e instanceof Reportable rep)
                rep.printSummary();
        }
        System.out.println();
    }
}
