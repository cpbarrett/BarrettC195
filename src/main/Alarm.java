package main;

import Model.Appointment;
import Model.Customer;
import Model.CustomerDatabaseModel;
import javafx.application.Platform;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class Alarm implements Runnable{
    public Alarm(){
    }
    @Override
    public void run() {
        try {
            long a = findSoonestAppointment().toLocalDateTime().toInstant(ZoneOffset.UTC).toEpochMilli();
            long b = Instant.now().toEpochMilli();
            long time = a - b;
            System.out.println("Seconds until Alarm rings: " + time /1000);
            if (time < 0) {
                return;
            }
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //This lambda is required in order to make use of the Platform.runLater method.
        //This method runs alert on the main gui thread instead of the alarm timer thread
        Platform.runLater(() -> alert());
    }
    private Timestamp findSoonestAppointment(){
        Timestamp soonest = Timestamp.valueOf("2021-01-01 00:00:00");
        List<Timestamp> times = new ArrayList<>();
        for (Customer customer: CustomerDatabaseModel.getCustomerList().getAllCustomers()){
            for (Appointment appointment: customer.getCustomerAppointments()) {
                times.add(Timestamp.valueOf(appointment.getStartTime()));
            }
        }
        for (Timestamp timestamp: times){
            if (timestamp.after(times.get(0))) {
                if (timestamp.before(soonest)){
                    soonest = timestamp;
                }
            }
        }
        return soonest;
    }
    private void alert(){
        AlertUser.display("Alert", "Appointment Time! Click ok to start web cam.");
    }
}
