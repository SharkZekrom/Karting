package modelPackage;

public class Category {

    private String label;
    private int speedLimit;

    public Category(String label, int speedLimit) {
        this.label = label;
        this.speedLimit = speedLimit;
    }

    public String getLabel() {
        return label;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    @Override
    public String toString() {
        return label;
    }
}
