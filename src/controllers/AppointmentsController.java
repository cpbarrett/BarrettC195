package controllers;

import Model.Appointment;
import Model.CustomerDatabaseModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    @Override
    public void initialize(URL url, ResourceBundle rs){
        createTable();
    }
    private void createTable(){
        apptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        apptName.setCellValueFactory(new PropertyValueFactory<>("apptName"));
        apptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        apptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptURL.setCellValueFactory(new PropertyValueFactory<>("url"));
//        apptStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
//        apptEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
//        appointments.setItems(CustomerDatabaseModel.getCustomerList().getAllCustomers());
    }
}
