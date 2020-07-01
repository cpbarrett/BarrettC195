package controllers;

import Model.City;
import Model.Customer;
import Model.CustomerDatabaseModel;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.AlertUser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML Button saveButton;
    @FXML TextField customerIdField;
    @FXML TextField customerNameField;
    @FXML TextField customerAddressField;
    @FXML ChoiceBox<String> customerCityChoice;
    @FXML TextField customerPostalCodeField;
    @FXML TextField phoneNumberField;
    @Override
    public void initialize(URL url, ResourceBundle rb){
        loadCityChoices();
    }

    private void loadCityChoices(){
        ObservableList<String> cityChoices = FXCollections.observableArrayList();
        for (City city: CustomerDatabaseModel.getLocationList().getCityList()){
            cityChoices.add(city.getCityName());
        }
        customerCityChoice.setItems(cityChoices);
    }

    public void loadCustomer(Customer customer){
        //updates the saveBtn functionality to run updateCustomer
        saveButton.setOnAction(actionEvent -> {
            try {
                updateCustomer(actionEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        customerIdField.setText(customer.getId()+"");
        customerNameField.setText(customer.getCustomerName());
        customerAddressField.setText(customer.getAddress());
        customerCityChoice.setValue(customer.getCity());
        customerPostalCodeField.setText(customer.getPostalCode()+"");
        phoneNumberField.setText(customer.getPhoneNumber());
    }

    @FXML
    private void saveCustomer(ActionEvent actionEvent) throws IOException {
        if (validateCustomerEntry()) {
            String selectedCity = customerCityChoice.getSelectionModel().getSelectedItem();
            City choice = CustomerDatabaseModel.getLocationList().lookupCity(selectedCity);
            Customer newCustomer = new Customer(
                    CustomerDatabaseModel.getCustomerList().getAllCustomers().size() + 1,
                    customerNameField.getText(),
                    customerAddressField.getText(),
                    choice.getCityName(),
                    choice.getCountry(),
                    Integer.parseInt(customerPostalCodeField.getText()),
                    phoneNumberField.getText()
            );
            CustomerDatabaseModel.insertNewCustomer(newCustomer);
            exitWindow(actionEvent);
        }
    }
    @FXML
    private void cancelCustomer(ActionEvent actionEvent) throws IOException {
        if (AlertUser.confirmDelete("Confirm Cancel?", "Are you sure you want to cancel?")){
            exitWindow(actionEvent);
        }
    }
    @FXML
    private void updateCustomer(ActionEvent actionEvent) throws IOException {
        if (validateCustomerEntry()) {
            String selectedCity = customerCityChoice.getSelectionModel().getSelectedItem();
            City choice = CustomerDatabaseModel.getLocationList().lookupCity(selectedCity);
            Customer updatedCustomer = new Customer(
                    Integer.parseInt(customerIdField.getText()),
                    customerNameField.getText(),
                    customerAddressField.getText(),
                    choice.getCityName(),
                    choice.getCountry(),
                    Integer.parseInt(customerPostalCodeField.getText()),
                    phoneNumberField.getText()
            );
            if (AlertUser.confirmDelete("Confirm Update?", "Are you sure you want to update this customer?")){
                CustomerDatabaseModel.updateCustomerDB(updatedCustomer);
                exitWindow(actionEvent);
            }
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
    private boolean validateCustomerEntry() {
        try {
            Integer.parseInt(customerPostalCodeField.getText().trim());
        } catch (NumberFormatException e) {
            AlertUser.showError("Please be sure that your postal code is a 5 digit number like: 12345");
            return false;
        }
        try {
            String validName = customerNameField.getText();
            String validAddress = customerAddressField.getText();
            String validCity = customerCityChoice.getSelectionModel().getSelectedItem();
            String validPostalCode = customerPostalCodeField.getText();
            String validNumber = phoneNumberField.getText();
            if (validName.isEmpty() || validAddress.isEmpty() || validCity.isEmpty() || validNumber.isEmpty()) {
                throw new NullPointerException();
            }
            if (validAddress.length() > 50) {
                AlertUser.showError("Address is too long.");
                return false;
            }
            if (validName.length() > 45) {
                AlertUser.showError("Phone Number is too long.");
                return false;
            }
            if (validPostalCode.length() > 10) {
                AlertUser.showError("Postal code is too long.");
                return false;
            }
        } catch (NullPointerException e){
            AlertUser.showError("Please ensure no categories are blank.");
            return false;
        }
        return true;
    }
}
