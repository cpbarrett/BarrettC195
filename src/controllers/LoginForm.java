package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.AlertUser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginForm implements Initializable {
    @FXML private Label greeting;
    @FXML private TextField username;
    @FXML private PasswordField password;
    ResourceBundle resourceBundle;
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

    }

    private boolean checkLogin(String user, String passW) {
        if (user.matches("test") && passW.matches("test")){
            return true;
        } else {
            AlertUser.display(resourceBundle.getString("alert"), resourceBundle.getString("alertMessage"));
            return false;
        }
    }

    @FXML
    private void openNewWindow(ActionEvent event) throws IOException {
            if (checkLogin(username.getText().trim(), password.getText().trim())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Customers.fxml"));
                Parent customerUI = loader.load();
                Scene scene = new Scene(customerUI);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setTitle("Customers");
                window.setScene(scene);
                window.show();
            }
    }

    @FXML
    private void exitWindow(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();
        System.exit(0);
    }
}
