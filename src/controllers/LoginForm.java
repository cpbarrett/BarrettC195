package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.AlertUser;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginForm implements Initializable {
    @FXML private TextField username;
    @FXML private PasswordField password;
    ResourceBundle resourceBundle;
    private PrintWriter printWriter;
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        try {
            this.printWriter = new PrintWriter(new FileOutputStream(new File("login.txt"), true));
        } catch (FileNotFoundException e) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    public ResourceBundle loadLanguageResources() {
        switch (Locale.getDefault().getCountry().trim()) {
            case "MX":
                this.resourceBundle = ResourceBundle.getBundle("ResourceBundle/MX", Locale.getDefault());
                break;
            case "CAN":
                this.resourceBundle = ResourceBundle.getBundle("ResourceBundle/CAN", Locale.getDefault());
                break;
            default :
                this.resourceBundle = ResourceBundle.getBundle("ResourceBundle/US", Locale.getDefault());
                break;
        }
        return resourceBundle;
    }
    private boolean checkLogin(String user, String passW) {
        if (user.matches("test") && passW.matches("test")){
            printWriter.append("User: " + user + "Time: " + LocalDateTime.now());
            printWriter.close();
            return true;
        } else {
            AlertUser.display(resourceBundle.getString("alert"), resourceBundle.getString("alertMessage"));
            return false;
        }
    }

    @FXML
    private void openNewWindow(ActionEvent event) throws IOException {
        String user = username.getText().trim();
        String pass = password.getText().trim();
            if (checkLogin(user, pass)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CustomerDatabaseView.fxml"));
                Parent customerUI = loader.load();
                Scene scene = new Scene(customerUI);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setTitle("Customers");
                window.setScene(scene);
                window.show();
            }
    }

    @FXML
    private void exitWindow(ActionEvent actionEvent) {
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();
        System.exit(0);
    }
}
