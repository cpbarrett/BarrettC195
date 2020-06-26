package Model;

import java.sql.Time;

public class Appointment {
    private int id;
    private Customer associatedCustomer;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
//    private Time startTime;
//    private Time endTime;


    public Appointment(int id, Customer associatedCustomer, String title, String description, String location, String contact, String type, String url) {
        this.id = id;
        this.associatedCustomer = associatedCustomer;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getAssociatedCustomer() {
        return associatedCustomer;
    }

    public void setAssociatedCustomer(Customer associatedCustomer) {
        this.associatedCustomer = associatedCustomer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
