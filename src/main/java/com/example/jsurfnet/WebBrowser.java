package com.example.jsurfnet;
import com.example.jsurfnet.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.io.File;


public class WebBrowser extends Application {

    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent loginRoot = loginLoader.load();
        Scene loginScene = new Scene(loginRoot);

        primaryStage.setScene(loginScene);
        primaryStage.show();

        LoginController loginController = loginLoader.getController();

        loginController.setLoginListener(event -> {
            // Authenticate user
            if (authenticateUser()) {
                // Load browser screen
                Parent browserRoot = null;
                try {
                    browserRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/WebBrowser.fxml")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene browserScene = new Scene(browserRoot);
                primaryStage.setScene(browserScene);
                primaryStage.show();
            } else {

            }
        });
    }

    private boolean authenticateUser() {
        return true;
    }

    public static void main(String[] args) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        //Finally works
        //Try this lads
        launch(args);
    }
}