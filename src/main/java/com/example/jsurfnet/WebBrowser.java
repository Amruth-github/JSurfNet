package com.example.jsurfnet;

import com.example.jsurfnet.controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

import javafx.scene.control.Tab;
import javafx.scene.web.WebView;

import com.example.jsurfnet.controllers.TabsController;

public class WebBrowser extends Application {

    private final int screenMinWidth = 800 , screenMinHeight = 600;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Browser.fxml")));
        primaryStage.setTitle("JSurfNet");
        Scene scene = new Scene(root, 800, 600);

        // Set the stage to full screen
        primaryStage.setFullScreen(true);

        // Get the screen dimensions and set the scene dimensions accordingly
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//        scene.setWidth(screenBounds.getWidth());
//        scene.setHeight(screenBounds.getHeight());

        primaryStage.setScene(scene);
        primaryStage.show();

        Parent tab = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Tabs.fxml")));
        primaryStage.setScene(new Scene(tab, 800, 600));
        primaryStage.setX(0);
        primaryStage.setY(50);

        primaryStage.setScene(scene);
        primaryStage.show();

//        TabsController tc = new TabsController();
//        if(tc.getNewBookmarkButton()!=null){
//            tc.getNewBookmarkButton().setOnAction(event -> {
//                Stage popup = new Stage();
//                TextField textField = new TextField();
//                StackPane popupRoot = new StackPane(textField);
//                popup.setScene(new Scene(popupRoot, 200, 50));
//                popup.show();
//            });
//        }
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