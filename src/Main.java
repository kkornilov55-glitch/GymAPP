import java.util.Scanner;

public class Main {
    private Scanner in = new Scanner(System.in);
    private WorkoutLog wlog = new WorkoutLog();

    private final String MAIN_MENU = """
                    
                                     МЕНЮ
                    -----------------------------------------
                    \t1. Добавить упражнение
                    \t2. Вывести отчет по тренеровке
                    \t3. Выйти
                    -----------------------------------------
                    """;
    private final String ADD_EXERCISE_MENU = """
                        
                        -----------------------------------------
                                 ДОБАВЛЕНИЕ УПРАЖНЕНИЯ
                        -----------------------------------------
                        Выберите механику выполнения:
                        
                        \t1. На повторения (пример: подтягивания)
                        \t2. На время (пример: планка)
                        -----------------------------------------
                        """;
    private final String ERROR_INVALID_CHOICE = """
                        
                        ---
                        Ошибка: некорректный номер действия.
                        ---
                        
                        """;
    private final String ERROR_NOT_A_NUMBER = """
                        
                        ---
                        Ошибка: пожалуйста, введите целое число.
                        ---
                        
                        """;
    private final String SUCCESS_EXERCISE_ADDED = """
            ------
            Упражнение успешно добавлено!
            ------
            """;

    void main() {
        int choice;
        while (true) {
            System.out.print(MAIN_MENU);
            choice = readInt("Выберите действие: ");

            switch (choice) {
                case 1 -> addExercise();
                case 2 -> printReport();
                case 3 -> { return; }
                default -> System.out.print(ERROR_INVALID_CHOICE);
            }
        }
    }
    private void addExercise() {
        System.out.print(ADD_EXERCISE_MENU);
        int choice = readInt("Ваш выбор: ");
        if (choice < 1 || choice > 2) {
            System.out.println(ERROR_INVALID_CHOICE);
            return;
        }

        System.out.println("-----");
        String exerciseName = readStr("Название упражнения: ");
        String targetMuscleGroup = readStr("Целевая группа мышц: ");
        int sets = readInt("Количество подходов: ");

        int repsPerSet, durationSeconds;
        switch (choice) {
            case 1:
                repsPerSet = readInt("Количество повторений: ");
                RepsExercise repsExercise = new RepsExercise(exerciseName, targetMuscleGroup, sets, repsPerSet);
                wlog.addExercise(repsExercise);
                System.out.println(SUCCESS_EXERCISE_ADDED);
                break;
            case 2:
                durationSeconds = readInt("Время: ");
                TimedExercise timedExercise = new TimedExercise(exerciseName, targetMuscleGroup, sets, durationSeconds);
                wlog.addExercise(timedExercise);
                System.out.println(SUCCESS_EXERCISE_ADDED);
                break;
        }
    }
    private void printReport() {
        wlog.printReport();
    }
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int num = Integer.parseInt(in.nextLine());
                return num;
            } catch (NumberFormatException _) {
                System.out.println(ERROR_NOT_A_NUMBER);
            }
        }
    }
    private String readStr(String prompt) {
        System.out.print(prompt);
        return in.nextLine();
    }
}

