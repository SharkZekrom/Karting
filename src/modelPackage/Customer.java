package modelPackage;

import java.util.Date;

public class Customer {

    private String email;
    private String locality;
    private String postCode;
    private Integer addressNumber;
    private String street;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String gender;
    private String phoneNumber;
    private boolean isProfessional;

    public Customer(String email, String locality, String postCode, Integer addressNumber, String street, String firstName, String lastName, Date birthDate, String gender, String phoneNumber, boolean isProfessional) {
        this.email = email;
        this.locality = locality;
        this.postCode = postCode;
        this.addressNumber = addressNumber;
        this.street = street;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.isProfessional = isProfessional;
    }

    public String getEmail() {
        return email;
    }

    public String getLocality() {
        return locality;
    }

    public String getPostCode() {
        return postCode;
    }

    public Integer getAddressNumber() {
        return addressNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isProfessional() {
        return isProfessional;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public void setLocality(String Locality) {
        this.locality = Locality;
    }

    public void setPostCode(String PostCode) {
        this.postCode = PostCode;
    }

    public void setAddressNumber(Integer AddressNumber) {
        this.addressNumber = AddressNumber;
    }

    public void setStreet(String Street) {
        this.street = Street;
    }

    public void setFirstName(String FirstName) {
        this.firstName = FirstName;
    }

    public void setLastName(String LastName) {
        this.lastName = LastName;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIsProfessional(boolean isProfessional) {
        this.isProfessional = isProfessional;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}
