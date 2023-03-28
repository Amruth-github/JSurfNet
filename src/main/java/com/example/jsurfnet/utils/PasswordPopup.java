package com.example.jsurfnet.utils;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;

public class PasswordPopup extends Popup {
    private Label username_txt = new Label("Username");
    private TextField username = new TextField();

    private Label password_txt = new Label("Password");
    private PasswordField password = new PasswordField();
    private Button Save = new Button("Save");

    private PasswordManager pwm = null;

    private Button cancel = new Button("Cancel");

    public void setUsername(String user) {
        username.setText(user);
    }

    public void setPassword(String pass) {
        password.setText(pass);
    }

    public PasswordPopup(boolean saving) {
        this.setAutoHide(true);
        BorderPane bp = new BorderPane();
        //bp.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-padding: 5px;");

        username_txt.setStyle("color: white;");
        password_txt.setStyle("color:white;");

        VBox vBox = new VBox(25);
        HBox buttons = new HBox(25);
        vBox.getChildren().addAll(username_txt, username, password_txt, password);
        if (saving) {
            buttons.getChildren().addAll(Save, cancel);
        } else {
            buttons.getChildren().add(cancel);
        }
        vBox.setStyle("-fx-background-color:#37302E; -fx-background-radius: 20;");
        vBox.getChildren().add(buttons);
        bp.setCenter(vBox);

        this.getContent().add(bp);

        Rectangle rect = new Rectangle(bp.getPrefWidth(), bp.getPrefHeight());
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        rect.setStyle("-fx-background-color: #f37302E;");
        this.setAnchorLocation(AnchorLocation.CONTENT_TOP_RIGHT);

        cancel.setOnAction(actionEvent -> {
            this.hide();
        });
    }

}