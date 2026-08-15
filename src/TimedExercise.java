public class TimedExercise extends Exercise implements Reportable {
    private int durationSeconds;
    private int sets;

    public TimedExercise(String name, String targetMuscleGroup, int sets, int durationSeconds) {
        super(name, targetMuscleGroup);
        this.sets = sets;
        this.durationSeconds = durationSeconds;
    }
    @Override
    public double calculateTotalVolume() {
        return durationSeconds * sets;
    }
    @Override
    public void printSummary() {
        System.out.printf("[%s] %s — %d сек х %d \n", getTargetMuscleGroup(), getName(), durationSeconds, sets);
    }


}
