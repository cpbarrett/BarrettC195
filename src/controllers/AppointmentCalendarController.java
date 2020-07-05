package controllers;

import javafx.scene.control.DatePicker;
import main.AlertUser;
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
    @FXML private TableView<Appointment> calendarAppointments;
    @FXML private TableColumn<Appointment, String> customerNames;
    @FXML private TableColumn<Appointment, String> appointmentLocation;
    @FXML private TableColumn<Appointment, Integer> appointmentYear;
    @FXML private TableColumn<Appointment, DayOfWeek> appointmentWeekday;
    @FXML private TableColumn<Appointment, Month> appointmentMonth;
    @FXML private TableColumn<Appointment, Integer> appointmentDate;
    @FXML private TableColumn<Appointment, LocalTime> appointmentTime;
    @FXML private DatePicker selectedDate;
    ObservableList<Appointment> calendar;
    @Override
    public void initialize(URL location, ResourceBundle resources) { createAppointmentsTable(); }
    private void createAppointmentsTable() {
        calendar = FXCollections.observableArrayList();
        customerNames.setCellValueFactory(new PropertyValueFactory<>("name"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentYear.setCellValueFactory(new PropertyValueFactory<>("appointmentYear"));
        appointmentWeekday.setCellValueFactory(new PropertyValueFactory<>("appointmentWeekday"));
        appointmentMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentMonth"));
        appointmentDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDayOfMonth"));
        appointmentTime.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        populateCalendar();
        calendarAppointments.setItems(calendar);
    }
    private void populateCalendar(){
        for (Customer customer: CustomerDatabaseModel.getCustomerList().getCustomerObservableList()){
            calendar.addAll(customer.getCustomerAppointments());
        }
    }
    private boolean validDate(){
        if (selectedDate.getValue() != null){
            return true;
        } else {
            AlertUser.showError("Please select a date first.");
            return false;
        }
    }
    @FXML
    private void monthView(){
        if (validDate()){
            calendar.clear();
            for (Customer customer: CustomerDatabaseModel.getCustomerList().getCustomerObservableList()){
                for (Appointment appointment : customer.getCustomerAppointments()){
                    if (appointment.getAppointmentDate().getMonthValue() == selectedDate.getValue().getMonthValue()) {
                        calendar.add(appointment);
                    }
                }
            }
            calendarAppointments.setItems(calendar);
        }
    }
    @FXML
    private void weekView(){
        if (validDate()){
            calendar.clear();
            for (Customer customer: CustomerDatabaseModel.getCustomerList().getCustomerObservableList()){
                for (Appointment appointment : customer.getCustomerAppointments()){
                    if (appointment.getAppointmentDate().isAfter(selectedDate.getValue().minusDays(1)))
                        if (appointment.getAppointmentDate().isBefore(selectedDate.getValue().plusDays(8))) {
                            calendar.add(appointment);
                        }
                }
            }
            calendarAppointments.setItems(calendar);
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
