package com.example.jsurfnet;
import com.example.jsurfnet.controllers.LoginController;
import com.example.jsurfnet.utils.CurrentUser;
import com.example.jsurfnet.utils.MongoDriver;
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
    private static Scene scene;
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
            CurrentUser currentUser = CurrentUser.getInstance();
            currentUser.setUsername("guest");
            goToBrowser();
        });
    }

    public static  Scene getScene() {
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
        primaryStage.show();
    }
    public static void main(String[] args) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        launch(args);
        MongoDriver.getClient().close();
    }
}