package controllers;

import Model.Appointment;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.AlertUser;

import java.io.*;
import java.net.URL;
import java.util.*;

public class AppointmentsController implements Initializable {
    @FXML private TableView<Appointment> appointments;
    @FXML private TableColumn<Appointment, Integer> appointmentId;
    @FXML private TableColumn<Appointment, String> appointmentName;
    @FXML private TableColumn<Appointment, String> appointmentTitle;
    @FXML private TableColumn<Appointment, String> appointmentDescription;
    @FXML private TableColumn<Appointment, String> appointmentLocation;
    @FXML private TableColumn<Appointment, String> appointmentContact;
    @FXML private TableColumn<Appointment, String> appointmentType;
    @FXML private TableColumn<Appointment, String> appointmentURL;
    @FXML private TableColumn<Appointment, String> appointmentStartTime;
    @FXML private TableColumn<Appointment, String> appointmentEndTime;
    @FXML private ChoiceBox<String> reportType;
    private Customer customer;

    @Override
    public void initialize(URL url, ResourceBundle rs){
        createTable();
    }
    private void createTable(){
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointmentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentURL.setCellValueFactory(new PropertyValueFactory<>("url"));
        appointmentStartTime.setCellValueFactory(new PropertyValueFactory<>("localDateStartTime"));
        appointmentEndTime.setCellValueFactory(new PropertyValueFactory<>("localDateEndTime"));
        createReportTypes();
    }
    private void createReportTypes(){
        ObservableList<String> reports = FXCollections.observableArrayList();
        reports.addAll("Appointment Types By Month", "Consultant Schedule", "Extra");
        reportType.setItems(reports);
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
    private void openNewWindow(ActionEvent event, Parent newUI) {
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
    @FXML
    private void generateReport(ActionEvent actionEvent) {
        try {
            if (!reportType.getSelectionModel().getSelectedItem().isEmpty()) {
                String selectedReportType = reportType.getSelectionModel().getSelectedItem();
                switch (selectedReportType) {
                    case "Appointment Types By Month":
                        appointmentMonths();
                        break;
                    case "Consultant Schedule":
                        consultantSchedule();
                        break;
                    case "Extra":
                        extraReport();
                        break;
                    default:
                        break;
                }
            }
        } catch (FileNotFoundException | NullPointerException ignored) {}
    }
    private void appointmentMonths() throws FileNotFoundException {
        Map<String, Integer> report = new HashMap<>();
        for (Appointment appointment: customer.getCustomerAppointments()){
            String key = appointment.getAppointmentMonth() + "-" + appointment.getType();
            report.put(key, 0);
        }
        for (Appointment appointment : customer.getCustomerAppointments()){
            String key = appointment.getAppointmentMonth() + "-" + appointment.getType();
            if (report.containsKey(key)){
                report.put(key, report.get(key)+1);
            }
        }
        PrintWriter generatedReport = new PrintWriter(new FileOutputStream(new File("report.txt")));
        for (Map.Entry<String, Integer> map : report.entrySet()){
            generatedReport.write(map.getKey() + " Count: " + map.getValue());
            generatedReport.println();
        }
        generatedReport.close();
    }
    private void consultantSchedule() throws FileNotFoundException {
        List<String> report = new ArrayList<>();
        report.add("Schedule for " + customer.getCustomerName() + ": ");
        for (Appointment appointment: customer.getCustomerAppointments()){
            report.add(
                    "Location: " + appointment.getLocation() + ", " +
                    appointment.getAppointmentTime() + " " +
                    appointment.getAppointmentMonth() +  " " +
                    appointment.getAppointmentDayOfMonth());
        }
        PrintWriter generatedReport = new PrintWriter(new FileOutputStream(new File("report.txt")));

        for (String line : report){
            generatedReport.write(line);
            generatedReport.println();
        }
        generatedReport.close();
    }
    private void extraReport() throws FileNotFoundException {
        List<String> report = new ArrayList<>();
        report.add("List All Appointments for " + customer.getCustomerName() + ": ");
        for (Appointment appointment: customer.getCustomerAppointments()){
            report.add(
                    "Description: " + appointment.getDescription() + ", " +
                    appointment.getAppointmentTime() + " " +
                    appointment.getAppointmentMonth() +  " " +
                    appointment.getAppointmentDayOfMonth());
        }
        PrintWriter generatedReport = new PrintWriter(new FileOutputStream(new File("report.txt")));

        for (String line : report){
            generatedReport.write(line);
            generatedReport.println();
        }
        generatedReport.close();
    }
}
