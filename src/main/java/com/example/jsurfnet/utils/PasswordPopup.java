package com.example.jsurfnet.utils;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.util.Duration;

public class PasswordPopup extends Popup {
    private Label username_txt = new Label("Username");
    private TextField username = new TextField();

    private Label password_txt = new Label("Password");
    private PasswordField password = new PasswordField();
    private Button Save = new Button("Save");

    private Button done = new Button("Done");

    private Button cancel = new Button("Cancel");

    public String getUsername() {
        return username.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public void setUsername(String user) {
        username.setText(user);
    }

    public void setPassword(String pass) {
        password.setText(pass);
    }

    public PasswordPopup(boolean saving) {
        BorderPane bp = new BorderPane();
        bp.setStyle("-fx-background-color: #292929; -fx-padding: 20px;");

        username_txt.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        password_txt.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        ImageView copyIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/copy.png")));
        copyIcon.setFitWidth(16);
        copyIcon.setFitHeight(16);
        ImageView copyIcon1 = new ImageView(new Image(getClass().getResourceAsStream("/icons/copy.png")));
        copyIcon1.setFitWidth(16);
        copyIcon1.setFitHeight(16);


        Button copyItemUsername = new Button();
        Button copyItemPassword = new Button();

        copyItemUsername.setGraphic(copyIcon1);
        copyItemPassword.setGraphic(copyIcon);

        copyItemUsername.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(username.getText());
            clipboard.setContent(content);
        });

        copyItemUsername.setOnMouseEntered(e -> {
            copyItemUsername.setText("Copy Username");
        });

        copyItemUsername.setOnMouseExited(e -> {
            copyItemUsername.setText("");
        });

        copyItemPassword.setOnMouseEntered(e -> {
            copyItemPassword.setText("Copy Password");
        });

        copyItemPassword.setOnMouseExited(e -> {
            copyItemPassword.setText("");
        });


        copyItemPassword.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(password.getText());
            clipboard.setContent(content);
        });

        done.setOnAction(actionEvent -> {
            this.hide();
        });

        VBox vBox = new VBox(20);
        HBox buttons = new HBox(20);
        vBox.getChildren().addAll(username_txt, username, copyItemUsername ,password_txt, password, copyItemPassword);
        if (saving) {
            buttons.getChildren().addAll(Save, cancel);
        } else {
            buttons.getChildren().addAll(done, cancel);
        }
        vBox.getChildren().add(buttons);
        vBox.setStyle("-fx-background-color: #3F3F3F; -fx-padding: 20px; -fx-spacing: 15px; -fx-border-radius: 10px;");
        bp.setCenter(vBox);

        this.getContent().add(bp);

        Rectangle rect = new Rectangle(bp.getPrefWidth(), bp.getPrefHeight());
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        rect.setStyle("-fx-fill: #292929;");
        this.setAnchorLocation(AnchorLocation.CONTENT_TOP_RIGHT);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), bp);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        Save.setStyle("-fx-background-color: #2d88ff; -fx-text-fill: #eeeeee;");
        cancel.setStyle("-fx-background-color: #d81e05; -fx-text-fill: #eeeeee;");

        ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(0.5), bp);
        scaleIn.setFromX(0);
        scaleIn.setToX(1);
        scaleIn.setFromY(0);
        scaleIn.setToY(1);

        ParallelTransition popupIn = new ParallelTransition(fadeIn, scaleIn);

        cancel.setOnAction(actionEvent -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), bp);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.5), bp);
            scaleOut.setFromX(1);
            scaleOut.setToX(0);
            scaleOut.setFromY(1);
            scaleOut.setToY(0);

            ParallelTransition popupOut = new ParallelTransition(fadeOut, scaleOut);

            popupOut.setOnFinished(event -> {
                this.hide();
            });

            popupOut.play();
        });

        popupIn.play();
    }


    public Button getSaveButton() {
        return Save;
    }
}
