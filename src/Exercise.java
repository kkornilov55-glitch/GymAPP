public abstract class Exercise {
    private String name;
    private String targetMuscleGroup;

    public void Exercise(String name, String targetMuscleGroup) {
        this.name = name;
        this.targetMuscleGroup = targetMuscleGroup;
    }
    public String getName() {
        return name;
    }
    public String getTargetMuscleGroup() {
        return targetMuscleGroup;
    }
    public abstract double calculateTotalVolume();
}
