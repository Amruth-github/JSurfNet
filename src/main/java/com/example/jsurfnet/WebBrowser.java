package com.example.jsurfnet;

import com.example.jsurfnet.controllers.TabsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

import javafx.scene.control.Tab;
import javafx.scene.web.WebView;

public class WebBrowser extends Application {

<<<<<<< Updated upstream
    private final int screenMinWidth = 800 , screenMinHeight = 600;
=======
    private Stage primaryStage;

    private static Scene scene;
>>>>>>> Stashed changes
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Browser.fxml")));
        primaryStage.setTitle("JSurfNet");
<<<<<<< Updated upstream
        primaryStage.setScene(new Scene(root, 800, 600));
=======
        Image i = new Image(new File("./icons/JSurfNet.png").toURI().toString());
        primaryStage.getIcons().add(i);

        primaryStage.setScene(loginScene);
        primaryStage.show();

        LoginController loginController = loginLoader.getController();

        loginController.setSignupListener(event -> {
            if (loginController.signup()) {
                goToBrowser();
            }
        });

        loginController.setLoginListener(event -> {
            if (loginController.authenticateUser()) {
                goToBrowser();
            }
        });

        loginController.setGuestListener(event->{
            goToBrowser();
        });
    }

    public static Scene getScene() {
        return scene;
    }

    void goToBrowser(){
        Parent browserRoot = null;
        try {
            browserRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/WebBrowser.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene browserScene = new Scene(browserRoot);
        scene = browserScene;
        primaryStage.setScene(browserScene);
>>>>>>> Stashed changes
        primaryStage.show();
    }

    public static double getVisualScreenWidth() {
        return Screen.getPrimary().getVisualBounds().getWidth();
    }

    public static double getVisualScreenHeight() {
        return Screen.getPrimary().getVisualBounds().getHeight();
    }

    public static void main(String[] args) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        //Finally works
        launch(args);
    }
}