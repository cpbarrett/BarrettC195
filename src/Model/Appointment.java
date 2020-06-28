package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

    public Appointment(int id, Customer associatedCustomer, String title, String description, String location, String contact, String type, String url, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.associatedCustomer = associatedCustomer;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        System.out.println(startTime.toString());
        this.startTime = "start";
        this.endTime = "end";
    }

    public String getStartTime() {
        return startTime;
    }
    public String getLocalStartTime() { return getLocalTime(startTime); }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }
    public String getLocalEndTime() {
        return getLocalTime(endTime);
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

    //moves clock forward 8 hours
    private String getLocalTime(String timestamp){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss.S");
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, dateTimeFormatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp conversion = Timestamp.from(utcZonedDateTime.toInstant());
        return conversion.toString();
    }
    //moves clock back 8 hrs
    public static String convertToUtcTime(String timeStamp){
        Timestamp utc = Timestamp.valueOf(timeStamp);
        LocalDateTime localDateTime = utc.toLocalDateTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp conversion = Timestamp.from(utcZonedDateTime.toInstant());
        return conversion.toString();
    }
}
