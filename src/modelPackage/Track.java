package modelPackage;

public class Track {

    private String label;
    private int length;
    private boolean isOfficial;
    private Address address;

    public Track(String label, int length, boolean isOfficial, Address address) {
        this.label = label;
        this.length = length;
        this.isOfficial = isOfficial;
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public int getLength() {
        return length;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public Address getAddress() {
        return address;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setOfficial(boolean isOfficial) {
        this.isOfficial = isOfficial;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return label;
    }
}
