package Model;

import java.sql.Timestamp;
import java.time.*;

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
    public String getLocalDateStartTime() { return convertToLocalDateTime(startTime); }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }
    public String getLocalDateEndTime() {
        return convertToLocalDateTime(endTime);
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

    private String convertToLocalDateTime(String time){
        Timestamp timestamp = Timestamp.valueOf(time);
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        localDateTime = localZonedDateTime.toLocalDateTime();
        return localDateTime.toString();
    }
    public static String convertToUtcDateTime(String time){
        Timestamp timestamp = Timestamp.valueOf(time);
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        localDateTime = utcZonedDateTime.toLocalDateTime();
        Timestamp finalTime = Timestamp.valueOf(localDateTime);
        return finalTime.toString();
    }
    public Month getAppointmentMonth(){
        Timestamp timestamp = Timestamp.valueOf(this.startTime);
        return timestamp.toLocalDateTime().getMonth();
    }
    public DayOfWeek getAppointmentWeekday(){
        Timestamp timestamp = Timestamp.valueOf(this.startTime);
        return timestamp.toLocalDateTime().getDayOfWeek();
    }
    public int getAppointmentDayOfMonth(){
        Timestamp timestamp = Timestamp.valueOf(this.startTime);
        return timestamp.toLocalDateTime().getDayOfMonth();
    }
    public LocalTime getAppointmentTime(){
        Timestamp timestamp = Timestamp.valueOf(this.startTime);
        ZonedDateTime zonedDateTime = timestamp.toInstant().atZone(ZoneId.of("UTC"));
        return zonedDateTime.toLocalTime();
    }
}
