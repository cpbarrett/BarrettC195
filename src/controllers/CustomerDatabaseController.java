package controllers;

import model.Customer;
import model.CustomerDatabaseModel;
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
import main.AlertUser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerDatabaseController implements Initializable {
    @FXML private TableView<Customer> customers;
    @FXML private TableColumn<Customer, Integer> customerId;
    @FXML private TableColumn<Customer, String> customerName;
    @FXML private TableColumn<Customer, String> customerAddress;
    @FXML private TableColumn<Customer, String> customerCity;
    @FXML private TableColumn<Customer, String> customerCountry;
    @FXML private TableColumn<Customer, String> customerPostalCode;
    @FXML private TableColumn<Customer, String> customerPhoneNumber;

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        if (!CustomerDatabaseModel.isConnected()) {
            CustomerDatabaseModel.getConnected();
        }
        createTable();
    }
    private void createTable(){
        customerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        customerCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customers.setItems(CustomerDatabaseModel.getCustomerList().getCustomerObservableList());
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
        customerController.loadCustomer(customers.getSelectionModel().getSelectedItem());
        openNewWindow(actionEvent, customerUI);
    }

    @FXML
    private void deleteCustomerAction(ActionEvent actionEvent){
        if (AlertUser.confirmDelete("Confirm Delete?", "Are you sure you want to delete this customer?")){
            CustomerDatabaseModel.deleteCustomerDB(customers.getSelectionModel().getSelectedItem());
            customers.setItems(CustomerDatabaseModel.getCustomerList().getCustomerObservableList());
        }
    }
    @FXML
    private void viewAppointments(ActionEvent actionEvent) throws IOException {
        if (!customers.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CustomerAppointments.fxml"));
            Parent appointmentsUI = loader.load();
            AppointmentsController appointmentsController = loader.getController();
            appointmentsController.loadCustomer(customers.getSelectionModel().getSelectedItem());
            openNewWindow(actionEvent, appointmentsUI);
        }
    }
    @FXML
    private void viewCalendar(ActionEvent actionEvent) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AppointmentCalendar.fxml"));
            Parent calendarUI = loader.load();
            openNewWindow(actionEvent, calendarUI);
    }
    private void openNewWindow(ActionEvent event, Parent newUI) {
        Scene scene = new Scene(newUI);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
