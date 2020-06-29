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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.AlertUser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable {
    @FXML private TableView<Appointment> appointments;
    @FXML private TableColumn<Appointment, Integer> apptId;
    @FXML private TableColumn<Appointment, String> apptName;
    @FXML private TableColumn<Appointment, String> apptTitle;
    @FXML private TableColumn<Appointment, String> apptDescription;
    @FXML private TableColumn<Appointment, String> apptLocation;
    @FXML private TableColumn<Appointment, String> apptContact;
    @FXML private TableColumn<Appointment, String> apptType;
    @FXML private TableColumn<Appointment, String> apptURL;
    @FXML private TableColumn<Appointment, String> apptStartTime;
    @FXML private TableColumn<Appointment, String> apptEndTime;
    private Customer customer;

    @Override
    public void initialize(URL url, ResourceBundle rs){
        createTable();
    }
    private void createTable(){
        apptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        apptName.setCellValueFactory(new PropertyValueFactory<>("name"));
        apptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        apptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptURL.setCellValueFactory(new PropertyValueFactory<>("url"));
        apptStartTime.setCellValueFactory(new PropertyValueFactory<>("localDateStartTime"));
        apptEndTime.setCellValueFactory(new PropertyValueFactory<>("localDateEndTime"));

    }
    public void loadCustomer(Customer customer){
        this.customer = customer;
        appointments.setItems(customer.getCustomerAppointments());
    }
    @FXML
    private void addAppointmentAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/MakeAppointment.fxml"));
        Parent appointmentUI = loader.load();
        AppointmentMaker appointmentMaker = loader.getController();
        appointmentMaker.loadCustomer(customer.getId());
        openNewWindow(actionEvent, appointmentUI);
    }
    @FXML
    private void updateAppointmentAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/MakeAppointment.fxml"));
        Parent appointmentUI = loader.load();
        AppointmentMaker appointmentMaker = loader.getController();
        appointmentMaker.loadCustomer(customer.getId());
        appointmentMaker.loadAppointment(appointments.getSelectionModel().getSelectedItem());
        openNewWindow(actionEvent, appointmentUI);
    }

    @FXML
    private void deleteAppointmentAction(ActionEvent actionEvent){
        if (AlertUser.confirmDelete("Confirm Delete?", "Are you sure you want to delete this appointment?")){
            CustomerDatabaseModel.deleteAppointment(appointments.getSelectionModel().getSelectedItem());
        }
    }
    @FXML
    private void returnToCustomerDatabaseView(ActionEvent actionEvent) throws IOException {
            exitWindow(actionEvent);
    }
    private void openNewWindow(javafx.event.ActionEvent event, Parent newUI) throws IOException {
        Scene scene = new Scene(newUI);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
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
}
