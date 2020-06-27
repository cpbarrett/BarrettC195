package Model;

import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private int id;
    private Customer associatedCustomer;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private String startTime;
    private String endTime;

    public Appointment(int id, Customer associatedCustomer, String title, String description, String location, String contact, String type, String url, String startTime, String endTime) {
        this.id = id;
        this.associatedCustomer = associatedCustomer;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
    public String getName(){ return this.associatedCustomer.getCustomerName();}

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
    @Override
    public String toString(){
        List<String> string = new ArrayList<>();
        string.add(this.getAssociatedCustomer().getId() +"");
        string.add("test");
        string.add(this.title);
        string.add(this.description);
        string.add(this.location);
        string.add(this.contact);
        string.add(this.type);
        string.add(this.url);
        string.add(this.startTime);
        string.add(this.endTime);
        String sql = string.toString();
        return  sql.substring(1,sql.length()-1);
    }
}
