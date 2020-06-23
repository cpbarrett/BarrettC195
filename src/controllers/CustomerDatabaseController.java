package controllers;

import Model.Customer;
import Model.CustomerDatabaseModel;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.AlertUser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerDatabaseController implements Initializable {
    @FXML private TableView customers;
    @FXML private TableColumn<Customer, SimpleStringProperty> lastName;
    @FXML private TableColumn<Customer, SimpleStringProperty> firstName;
    @FXML private TableColumn<Customer, SimpleStringProperty> phoneNumber;
    @FXML private TextField searchField;
    private ResourceBundle rb;

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        this.rb = rb;
        createTable();
//        Connection connect = CustomerDatabaseModel.getConnected();
//        ResultSet rs = connect.createStatement().executeQuery("SELECT * FROM *");

//        while(rs.next()){
//            customerData.add()
//        }
    }

    private void createTable(){
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customers.setItems(CustomerDatabaseModel.getCustomerList().getAllCustomers());
    }
    @FXML
    private void searchCustomerAction(ActionEvent actionEvent){
        try {
            customers.setItems((ObservableList) CustomerDatabaseModel.getCustomerList().lookupCustomer(Integer.parseInt(searchField.getText().trim())));
        } catch (NumberFormatException e) {
            customers.setItems(CustomerDatabaseModel.getCustomerList().lookupCustomer(searchField.getText().trim()));
        }
    }

    @FXML
    private void addCustomerAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AddCustomer.fxml"));
        Parent customerUI = loader.load();
        openNewWindow(actionEvent, customerUI);
    }

    @FXML
    private void updateCustomerAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AddCustomer.fxml"));
        Parent customerUI = loader.load();
        CustomerController customerController = loader.getController();
        openNewWindow(actionEvent, customerUI);
    }

    @FXML
    private void deleteCustomerAction(ActionEvent actionEvent){
        if (AlertUser.confirmDelete(rb.getString("alertDelete"), rb.getString("alertDeleteMessage"))){
            CustomerDatabaseModel.getCustomerList().deleteCustomer((Customer) customers.getSelectionModel().getSelectedItem());
        }
    }
    private void openNewWindow(ActionEvent event, Parent newUI) throws IOException {
        Scene scene = new Scene(newUI);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
