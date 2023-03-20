package com.example.jsurfnet;
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

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/WebBrowser.fxml")));
        primaryStage.setTitle("JSurfNet");
        Image i = new Image(new File("./icons/JSurfNet.png").toURI().toString());
        primaryStage.getIcons().add(i);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    public static void main(String[] args) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        //Finally works
        //Try this lads
        launch(args);
    }
}