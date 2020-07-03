package controllers;

import model.Appointment;
import model.City;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.AlertUser;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AppointmentMaker implements Initializable {
    @FXML private TextField appointmentIdField;
    @FXML private TextField appointmentDescriptionField;
    @FXML private ChoiceBox<String> appointmentLocationChoice;
    @FXML private TextField appointmentContactField;
    @FXML private TextField appointmentURLField;
    @FXML private ChoiceBox<String> appointmentTimeSlots;
    @FXML private DatePicker appointmentDateChoice;
    @FXML private TextField appointmentCustomerName;
    @FXML private TextField appointmentTypeField;
    @FXML private TextField appointmentTitleField;
    @FXML private Button saveButton;
    private Appointment appointment;
    private Customer customer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCities();
    }

    private void loadCities(){
        ObservableList<String> cityChoices = FXCollections.observableArrayList();
        for (City city: CustomerDatabaseModel.getLocationList().getCityList()){
            cityChoices.add(city.getCityName());
        }
        appointmentLocationChoice.setItems(cityChoices);
        loadBusinessHours();
    }
    private void loadBusinessHours(){
        ObservableList<String> timeSlots = FXCollections.observableArrayList();
        for (int i = 9; i < 12; i++) {
            timeSlots.add(i + " AM");
        }
        for (int i = 1; i < 5; i++) {
            timeSlots.add(i + " PM");
        }
        appointmentTimeSlots.setItems(timeSlots);
    }

    public void loadCustomer(int customerId){
        this.customer = CustomerDatabaseModel.getCustomerList().lookupCustomer(customerId-1);
        appointmentCustomerName.setText(customer.getCustomerName());
    }

    public void loadAppointment(Appointment appointment){
        this.appointment = appointment;
        //updates the saveBtn functionality to run updateCustomer
        saveButton.setOnAction(actionEvent -> {
            try {
                updateAppointment(actionEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        appointmentIdField.setText(appointment.getId()+"");
        appointmentCustomerName.setText(appointment.getAssociatedCustomer().getCustomerName());
        appointmentDescriptionField.setText(appointment.getDescription());
        appointmentLocationChoice.setValue(appointment.getLocation());
        appointmentContactField.setText(appointment.getContact());
        appointmentURLField.setText(appointment.getUrl());
        appointmentTitleField.setText(appointment.getTitle());
        appointmentTypeField.setText(appointment.getType());
        appointmentDateChoice.setValue(LocalDate.now());
        appointmentTimeSlots.setValue("Select New Time");
    }

    @FXML
    private void saveAppointment(ActionEvent actionEvent) throws IOException {
        if (validateAppointment()){
            CustomerDatabaseModel.makeNewAppointment(new Appointment(
                    CustomerDatabaseModel.getAppointmentTotal(),
                    customer,
                    appointmentTitleField.getText(),
                    appointmentDescriptionField.getText(),
                    appointmentLocationChoice.getSelectionModel().getSelectedItem(),
                    appointmentContactField.getText(),
                    appointmentTypeField.getText(),
                    appointmentURLField.getText(),
                    Appointment.convertToUtcDateTime(makeTimeStamp()),
                    Appointment.convertToUtcDateTime(appointmentDuration())
            ));
            exitWindow(actionEvent);
        }
    }
    @FXML
    private void updateAppointment(ActionEvent actionEvent) throws IOException {
        if (validateAppointment()) {
            if (AlertUser.confirmDelete("Confirm Update?", "Are you sure you want to update your appointment?")) {
                appointment.setTitle(appointmentTitleField.getText());
                appointment.setDescription(appointmentDescriptionField.getText());
                appointment.setLocation(appointmentLocationChoice.getSelectionModel().getSelectedItem());
                appointment.setContact(appointmentContactField.getText());
                appointment.setType(appointmentTypeField.getText());
                appointment.setUrl(appointmentURLField.getText());
                appointment.setStartTime(Appointment.convertToUtcDateTime(makeTimeStamp()));
                appointment.setEndTime(Appointment.convertToUtcDateTime(appointmentDuration()));
                CustomerDatabaseModel.updateAppointment(appointment);
                exitWindow(actionEvent);
            }
        }
    }
    @FXML
    private void cancelAppointment(ActionEvent actionEvent) throws IOException {
        if (AlertUser.confirmDelete("Confirm Cancel?", "Are you sure you want to cancel?")){
            exitWindow(actionEvent);
        }
    }
    private void exitWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CustomerDatabaseView.fxml"));
        Parent customerUI = loader.load();
        Scene scene = new Scene(customerUI);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Customers");
        window.setScene(scene);
        window.show();
    }
    private boolean validateAppointment(){
        try {
            appointmentTitleField.getText();
            appointmentDescriptionField.getText();
            appointmentLocationChoice.getSelectionModel().getSelectedItem();
            appointmentContactField.getText();
            appointmentTypeField.getText();
            appointmentURLField.getText();
            String time = Appointment.convertToUtcDateTime(makeTimeStamp());
            Appointment.convertToUtcDateTime(appointmentDuration());

            for (Customer customer: CustomerDatabaseModel.getCustomerList().getAllCustomers()){
                for (Appointment appointment: customer.getCustomerAppointments()) {
                    if (time.matches(appointment.getStartTime())){
                        AlertUser.showError("Appointment time is already taken please pick another time.");
                        return false;
                    }
                }
            }


        } catch (NullPointerException e){
            e.printStackTrace();
            AlertUser.showError("Ensure that no appointment fields are blank.");
        }
        return true;
    }
    private String makeTimeStamp(){
        LocalDate appointmentDate = appointmentDateChoice.getValue();
        String appointmentTime = appointmentTimeSlots.getSelectionModel().getSelectedItem();
        switch (appointmentTime) {
            case "10 AM":
                appointmentTime = " 10:00:00.0";
                break;
            case "11 AM":
                appointmentTime = " 11:00:00.0";
                break;
            case "1 PM":
                appointmentTime = " 13:00:00.0";
                break;
            case "2 PM":
                appointmentTime = " 14:00:00.0";
                break;
            case "3 PM":
                appointmentTime = " 15:00:00.0";
                break;
            case "4 PM":
                appointmentTime = " 16:00:00.0";
                break;
            default:
                appointmentTime = " 09:00:00.0";
                break;
        }
        return appointmentDate + appointmentTime;
    }
    private String appointmentDuration() { //1 hour
        LocalDate appointmentDate = appointmentDateChoice.getValue();
        String appointmentTime = appointmentTimeSlots.getSelectionModel().getSelectedItem();
        switch (appointmentTime) {
            case "10 AM":
                appointmentTime = " 11:00:00.0";
                break;
            case "11 AM":
                appointmentTime = " 12:00:00.0";
                break;
            case "1 PM":
                appointmentTime = " 14:00:00.0";
                break;
            case "2 PM":
                appointmentTime = " 15:00:00.0";
                break;
            case "3 PM":
                appointmentTime = " 16:00:00.0";
                break;
            case "4 PM":
                appointmentTime = " 17:00:00.0";
                break;
            default:
                appointmentTime = " 10:00:00.0";
                break;
        }
        return appointmentDate + appointmentTime;
    }
}
