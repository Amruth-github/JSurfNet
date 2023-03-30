package com.example.jsurfnet;
import com.example.jsurfnet.controllers.LoginController;
import com.example.jsurfnet.utils.CurrentUser;
import com.example.jsurfnet.utils.MongoDriver;
import javafx.application.Application;
import javafx.application.Platform;
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
    private static Scene scene;

    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {

        this.primaryStage = primaryStage;

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent loginRoot = loginLoader.load();
        Scene loginScene = new Scene(loginRoot);

        primaryStage.setTitle("JSurfNet");
        Image i = new Image(new File("./icons/JSurfNet.png").toURI().toString());
        primaryStage.getIcons().add(i);

        primaryStage.setScene(loginScene);
        primaryStage.show();

        LoginController loginController = loginLoader.getController();

        loginController.setSignupListener(event -> {
            try {
                if (loginController.signup()) {
                    goToBrowser();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        loginController.setLoginListener(event -> {
            try {
                if (loginController.authenticateUser()) {
                    goToBrowser();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        loginController.setGuestListener(event->{
            CurrentUser currentUser = CurrentUser.getInstance();
            currentUser.setUsername("guest", "");
            goToBrowser();
        });

//        loginController.setLocalUser(event -> {
//            try {
//                if (loginController.authenticateUser()) {
//                    goToBrowser();
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });

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
        primaryStage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
        });
    }



    public static void main(String[] args) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        launch(args);
        MongoDriver.getClient().close();
    }
}