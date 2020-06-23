package controllers;

import Model.Customer;
import Model.CustomerDatabase;
import Model.CustomerList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML private TableView customers;
    @FXML private TableColumn<Customer, SimpleStringProperty> lastName;
    @FXML private TableColumn<Customer, SimpleStringProperty> firstName;
    @FXML private TableColumn<Customer, SimpleStringProperty> phoneNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Connection connect = CustomerDatabase.getConnected();
//        ResultSet rs = connect.createStatement().executeQuery("SELECT * FROM *");

//        while(rs.next()){
//            customerData.add()
//        }
    }

    private void createTable(){
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customers.setItems(CustomerDatabase.getCustomerList().getCustomerObservableList());
    }
    @FXML
    private void searchCustomerAction(ActionEvent actionEvent){

    }

    @FXML
    private void addCustomerAction(ActionEvent actionEvent){

    }

    @FXML
    private void updateCustomerAction(ActionEvent actionEvent){

    }

    @FXML
    private void deleteCustomerAction(ActionEvent actionEvent){

    }
}
