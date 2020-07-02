package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {
    private final int id;
    private final String customerName;
    private final String address;
    private final String city;
    private final String country;
    private final int postalCode;
    private final String phoneNumber;
    private final ObservableList<Appointment> customerAppointments;

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

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public int getPostalCode() {
        return postalCode;
    }
    public String getCountry() { return country; }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ObservableList<Appointment> getCustomerAppointments() {
        return customerAppointments;
    }
    public void addAppointment(Appointment appointment){
        this.customerAppointments.add(appointment);
    }
    public void deleteAppointment(Appointment appointment){
        this.customerAppointments.remove(appointment);
    }
}
