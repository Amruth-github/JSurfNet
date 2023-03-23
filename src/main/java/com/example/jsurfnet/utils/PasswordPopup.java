package com.example.jsurfnet.utils;

import com.example.jsurfnet.WebBrowser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;

public class PasswordPopup extends Popup {
    private Label username_txt = new Label("Username");
    private TextField username = new TextField();

    private Label password_txt = new Label();
    private PasswordField password = new PasswordField();
    private Button Save = new Button("Save");

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
        bp.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-padding: 5px;");

        VBox vBox = new VBox(getWidth());
        HBox buttons = new HBox(getHeight());
        vBox.getChildren().addAll(username_txt, username, password_txt, password);
        if (saving == true) {
            buttons.getChildren().addAll(Save, cancel);
        } else {
            buttons.getChildren().add(cancel);
        }
        vBox.setStyle("-fx-background-color: #223236;");
        vBox.getChildren().add(buttons);
        bp.setCenter(vBox);

        this.getContent().add(bp);

        Rectangle rect = new Rectangle(bp.getPrefWidth(), bp.getPrefHeight());
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        this.setAnchorLocation(AnchorLocation.CONTENT_TOP_RIGHT);

        Save.setOnAction(actionEvent -> {

        });

        cancel.setOnAction(actionEvent ->  {
            this.hide();
        });
    }

}
