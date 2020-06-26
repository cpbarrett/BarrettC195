package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {
    private int id;
    private String customerName;
    private String address;
    private String city;
    private String country;
    private int postalCode;
    private String phoneNumber;
    private ObservableList<Appointment> customerAppointments;

    public Customer(int id, String customerName, String address, String city, String country, int postalCode, String phoneNumber){
        this.id = id;
        this.customerName = customerName;
        this.address = address;
        this.city = city;
        this. country = country;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        customerAppointments = FXCollections.observableArrayList();
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ObservableList<Appointment> getCustomerAppointments() {
        return customerAppointments;
    }
}
