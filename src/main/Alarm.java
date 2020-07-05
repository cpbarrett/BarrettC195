package main;
import model.Appointment;
import model.Customer;
import model.CustomerDatabaseModel;
import javafx.application.Platform;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Alarm implements Runnable{
    private Timestamp soonest;
    public Alarm(){
        soonest = Timestamp.valueOf("2030-01-01 00:00:00");
    }
    @Override
    public void run() {
        try {
            long time = checkTime();
            checkAlarm();
            while (time > 1000){
                Thread.sleep(1000);
                time = checkTime();
            }
            if (time < 1000 && time > 0) {
                Thread.sleep(1000);
                alert();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private Timestamp findSoonestAppointment(){
        Timestamp rightNow = Timestamp.from(Instant.now());
        List<Timestamp> times = new ArrayList<>();

        for (Customer customer: CustomerDatabaseModel.getCustomerList().getCustomerObservableList()){
            for (Appointment appointment: customer.getCustomerAppointments()) {
                times.add(Timestamp.valueOf(appointment.getLocalDateStartTime()));
            }
        }

        for (Timestamp timestamp: times){
            if (timestamp.after(rightNow)) {
                if (timestamp.before(soonest)){
                    soonest = timestamp;
                }
            }
        }
        return soonest;
    }
    private long checkTime(){
        long a = findSoonestAppointment().toInstant().toEpochMilli();
        long b = Instant.now().toEpochMilli();
        return a - b;
    }
    private void alert(){
        //This lambda is required in order to make use of the Platform.runLater method.
        //This method runs alert on the main gui thread instead of the alarm timer thread
        Platform.runLater(() -> {
            AlertUser.display("Alert", "Appointment Time! Click ok to start web cam.");
            Main.setAlarm();
        });
    }
    private void checkAlarm(){
        System.out.println("Alarm Set For: " + soonest);
    }
}
