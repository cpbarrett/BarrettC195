package controllers;

import Model.Appointment;
import Model.Customer;
import Model.CustomerDatabaseModel;
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
import java.util.ResourceBundle;

public class AppointmentMaker implements Initializable {
    @FXML private TextField apptIdField;
    @FXML private TextField appointmentDescriptionField;
    @FXML private ChoiceBox apptLocationChoice;
    @FXML private TextField apptContactField;
    @FXML private TextField apptURLField;
    @FXML private TextField apptTimeField;
    @FXML private DatePicker apptDateChoice;
    @FXML private TextField appointmentCustomerName;
    @FXML private TextField apptTypeField;
    @FXML private TextField apptTitleField;
    @FXML private Button saveButton;
    private Appointment appointment;
    private Customer customer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCities();
    }

    private void loadCities(){
        apptLocationChoice.setItems(CustomerDatabaseModel.getLocationList().getCityList());
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
//        customerIdField.setText(appointment.getId()+"");
//        customerNameField.setText(appointment.getCustomerName());
//        customerAddressField.setText(appointment.getAddress());
//        customerCityChoice.setValue(appointment.getCity());
//        customerPostalCodeField.setText(appointment.getPostalCode()+"");
//        phoneNumberField.setText(appointment.getPhoneNumber());
    }

    @FXML
    private void saveAppointment(ActionEvent actionEvent) throws IOException {
        if (validateAppointment()){
            CustomerDatabaseModel.makeNewAppointment(new Appointment(
                    customer.getCustomerAppointments().size()+1,
                    customer,
                    apptTitleField.getText(),
                    appointmentDescriptionField.getText(),
                    apptLocationChoice.getSelectionModel().getSelectedItem().toString(),
                    apptContactField.getText(),
                    apptTypeField.getText(),
                    apptURLField.getText(),
                    apptTimeField.getText(),
                    apptTimeField.getText()
            ));
            exitWindow(actionEvent);
        } else {
            exitWindow(actionEvent);
        }
    }
    @FXML
    private void updateAppointment(ActionEvent actionEvent) throws IOException {
        if (validateAppointment()){
            if (AlertUser.confirmDelete("Confirm Update?", "Are you sure you want to update your appointment?")){
                exitWindow(actionEvent);
            }
        } else {
            exitWindow(actionEvent);
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
            apptTitleField.getText();
            appointmentDescriptionField.getText();
            apptLocationChoice.getSelectionModel().getSelectedItem().toString();
            apptContactField.getText();
            apptTypeField.getText();
            apptURLField.getText();
            apptTimeField.getText();
            apptTimeField.getText();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return true;
    }
}
