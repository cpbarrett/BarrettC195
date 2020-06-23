package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    @Override
    public void start(Stage window) throws Exception{
//        Locale.setDefault(new Locale("fr", "CAN"));
        Parent loginUI = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/LoginForm.fxml"));
            loginUI = loader.load();
            switch (Locale.getDefault().getCountry().trim()) {
                case "MX":
                    loader.setResources(ResourceBundle.getBundle("ResourceBundle/MX", Locale.getDefault()));
                    break;
                case "CAN":
                    loader.setResources(ResourceBundle.getBundle("ResourceBundle/CAN", Locale.getDefault()));
                    break;
                default :
                    loader.setResources(ResourceBundle.getBundle("ResourceBundle/US", Locale.getDefault()));
                    break;
            }
            window.setTitle(loader.getResources().getString("title"));
            window.setScene(new Scene(loginUI, 600, 400));
            window.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
