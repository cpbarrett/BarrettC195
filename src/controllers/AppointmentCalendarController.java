package controllers;

import model.Appointment;
import model.Customer;
import model.CustomerDatabaseModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Month;
import java.util.ResourceBundle;

public class AppointmentCalendarController implements Initializable {
    @FXML TableView<Appointment> calendarAppointments;
    @FXML TableColumn<Appointment, String> customerNames;
    @FXML TableColumn<Appointment, String> appointmentLocation;
    @FXML TableColumn<Appointment, DayOfWeek> appointmentWeek;
    @FXML TableColumn<Appointment, Month> appointmentMonth;
    @FXML TableColumn<Appointment, Integer> appointmentDate;
    @FXML TableColumn<Appointment, LocalTime> appointmentTime;
    ObservableList<Appointment> calendar;
    @Override
    public void initialize(URL location, ResourceBundle resources) { createAppointmentsTable(); }
    private void createAppointmentsTable() {
        calendar = FXCollections.observableArrayList();
        customerNames.setCellValueFactory(new PropertyValueFactory<>("name"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentWeek.setCellValueFactory(new PropertyValueFactory<>("appointmentWeekday"));
        appointmentMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentMonth"));
        appointmentDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDayOfMonth"));
        appointmentTime.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        populateCalendar();
        calendarAppointments.setItems(calendar);
    }
    private void populateCalendar(){
        for (Customer customer: CustomerDatabaseModel.getCustomerList().getAllCustomers()){
            calendar.addAll(customer.getCustomerAppointments());
        }
    }
    @FXML
    private void exitWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CustomerDatabaseView.fxml"));
        Parent customerUI = loader.load();
        Scene scene = new Scene(customerUI);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Customers");
        window.setScene(scene);
        window.show();
    }
}
