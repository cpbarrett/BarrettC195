package controllers;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.AlertUser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML Button saveButton;
    @FXML TextField firstNameField;
    @FXML TextField lastNameField;
    @FXML TextField phoneNumberField;
    ResourceBundle rb;
    @Override
    public void initialize(URL url, ResourceBundle rb){
        this.rb = rb;
    }

    public void loadCustomer(Customer customer){
        saveButton.setOnAction(actionEvent -> {
            try {
                updateCustomer(actionEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        phoneNumberField.setText(customer.getPhoneNumber());
    }

    @FXML
    private void saveCustomer(ActionEvent actionEvent) throws IOException {
        Customer newCustomer = new Customer(0, firstNameField.getText(), lastNameField.getText(), phoneNumberField.getText());
        CustomerDatabaseModel.getCustomerList().addCustomer(newCustomer);
        exitWindow(actionEvent);

    }
    @FXML
    private void cancelCustomer(ActionEvent actionEvent) throws IOException {
        if (AlertUser.confirmDelete(rb.getString("cancel"), rb.getString("cancelMessage"))){
            exitWindow(actionEvent);
        }
    }
    @FXML
    private void updateCustomer(ActionEvent actionEvent) throws IOException {
        exitWindow(actionEvent);
    }
    private void exitWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CustomerDatabaseView.fxml"));
        Parent customerUI = loader.load();
        Scene scene = new Scene(customerUI);
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
