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

    private final int screenMinWidth = 800 , screenMinHeight = 600;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Browser.fxml")));
        primaryStage.setTitle("JSurfNet");
        primaryStage.setScene(new Scene(root, 800, 600));
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