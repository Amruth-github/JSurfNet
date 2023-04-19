package com.example.jsurfnet;
import com.example.jsurfnet.controllers.LoginController;
import com.example.jsurfnet.services.MongoDriver;
import com.example.jsurfnet.singleton.CurrentUser;
import com.example.jsurfnet.singleton.ToolBar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.swing.*;


public class WebBrowser extends Application {

    private Stage primaryStage;
    private static Scene scene;

    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent loginRoot = loginLoader.load();
        Scene loginScene = new Scene(loginRoot);

        primaryStage.setTitle("JSurfNet");
        Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/JSurfNet.png")));
        primaryStage.getIcons().add(i);

        primaryStage.setScene(loginScene);
        primaryStage.show();

        LoginController loginController = loginLoader.getController();

        loginController.signupButton.setOnAction(actionEvent -> {
            try {
                if (loginController.signup()) {
                    goToBrowser();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        loginController.loginButton.setOnAction(actionEvent -> {
            try {
                if (loginController.authenticateUser()) {
                    goToBrowser();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        loginController.guestButton.setOnAction(actionEvent -> {
            CurrentUser currentUser = CurrentUser.getInstance();
            currentUser.setUsername("guest", "");
            goToBrowser();
        });
    }

    public static  Scene getScene() {
        return scene;
    }

    public static  Stage getStage() {return stage;}
    void goToBrowser(){
        Parent browserRoot = null;
        try {
            browserRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/WebBrowser.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene browserScene = new Scene(browserRoot);
        browserScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
        scene = browserScene;
        stage = primaryStage;
        primaryStage.setScene(browserScene);
        primaryStage.show();
        ToolBar.getInstance().getLogoutButton().setOnAction(actionEvent -> {
            if (new File("JSurfNet.jar").exists()) {
                try {
                    Process p = Runtime.getRuntime().exec("java -jar JSurfNet.jar");
                    Platform.exit();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, e.toString());
                }
            }
        });
        primaryStage.setOnCloseRequest(windowEvent -> {
            MongoDriver.getClient().close();
            Platform.exit();
        });
    }



    public static void main(String[] args) {
        launch(args);
    }
}