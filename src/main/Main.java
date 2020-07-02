package main;

import Model.CustomerDatabaseModel;
import controllers.LoginForm;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    public static void setAlarm() {
        Alarm alarm = new Alarm();
        new Thread(alarm).start();
    }

    @Override
    public void start(Stage window) throws IOException {
        CustomerDatabaseModel.createCustomerList();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/LoginForm.fxml"));
        Parent loginUI = loader.load();
        LoginForm loginForm = loader.getController();
        window.setTitle(loginForm.loadLanguageResources().getString("title"));
        window.setScene(new Scene(loginUI, 600, 400));
        //Add listener to close out the database when the app exits
        window.setOnCloseRequest(event -> {
            try {
                if (CustomerDatabaseModel.getConnection() != null) {
                    CustomerDatabaseModel.getConnection().close();
                    System.out.println("Connection to database terminated.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Platform.exit();
            System.exit(0);
                });
        window.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
