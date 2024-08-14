package modelPackage;

public class Address {

    private int addressNumber;
    private String addressStreetLabel;

    public Address(int addressNumber, String addressStreetLabel) {
        this.addressNumber = addressNumber;
        this.addressStreetLabel = addressStreetLabel;
    }

    public int getAddressNumber() {
        return addressNumber;
    }

    public String getAddressStreetLabel() {
        return addressStreetLabel;
    }

    public void setAddressNumber(int addressNumber) {
        this.addressNumber = addressNumber;
    }

    public void setAddressStreetLabel(String addressStreetLabel) {
        this.addressStreetLabel = addressStreetLabel;
    }

    @Override
    public String toString() {
        return addressNumber + " , " + addressStreetLabel;
    }
}
