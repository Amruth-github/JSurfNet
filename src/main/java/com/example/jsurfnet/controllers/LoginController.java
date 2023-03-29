
package com.example.jsurfnet.controllers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.example.jsurfnet.utils.CurrentUser;
import com.example.jsurfnet.utils.MongoDriver;
import com.example.jsurfnet.utils.PasswordManager;
import com.example.jsurfnet.utils.SerializeUser;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.bson.Document;

import javax.print.Doc;
import javax.swing.JOptionPane;

public class LoginController implements Initializable {

    @FXML
    public Button signupButton;
    @FXML
    public Button guestButton;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    private MongoCollection<Document> usersCollection;
    private MongoClient mongoClient;


    public LoginController() {
        MongoDatabase database = MongoDriver.getMongo();
        this.usersCollection = database.getCollection("users");
    }

    public void setLoginListener(Consumer<ActionEvent> loginListener) {
        loginButton.setOnAction(loginListener::accept);
    }

    public void setSignupListener(Consumer<ActionEvent> signupListener) {
        signupButton.setOnAction(signupListener::accept);
    }

    public void setGuestListener(Consumer<ActionEvent> guestListener) {
        guestButton.setOnAction(guestListener::accept);
    }

//    public void setLocalUser(Consumer<ActionEvent> loginListener) {
//        loginButton.setOnAction(loginListener::accept);
//    }


    public boolean authenticateUser() throws IOException {

        if (CurrentUser.getInstance() != null) {
            return true;
        }

        Document user = usersCollection.find(new Document("username", usernameField.getText())).first();
        if (user != null) {
            if (passwordField.getText().strip().equals(user.getString("password"))){
                CurrentUser currentUser = CurrentUser.getInstance();
                currentUser.setUsername(usernameField.getText(), passwordField.getText());
                SerializeUser su = new SerializeUser();
                su.Serialize();
                return true;
            }
            else {
                JOptionPane.showMessageDialog(null, "Wrong Password", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Account does not exits, please Sign Up instead", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean signup() throws IOException {
        Document document = new Document();
        Document existingUser = usersCollection.find(new Document("username", usernameField.getText())).first();
        if (existingUser != null) {
            JOptionPane.showMessageDialog(null, "User already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            if (usernameField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Username field empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            document.append("username", usernameField.getText().strip());
            if (passwordField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Password field empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            document.append("password", passwordField.getText().strip());
            usersCollection.insertOne(document);
            Document d = new Document();
            d.append("user", usernameField.getText().strip());
            d.append("passwords", new PasswordManager().getSerialized());
            MongoDriver.getMongo().getCollection("password").insertOne(d);
            JOptionPane.showMessageDialog(null, "Signup successful!");
            CurrentUser currentUser = CurrentUser.getInstance();
            currentUser.setUsername(usernameField.getText(), passwordField.getText());
            SerializeUser su = new SerializeUser();
            su.Serialize();
            return true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File dir = new File("userprofiles");

        double y = 100.0;

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile() && file.getName().endsWith(".ser")) {
                String filename = file.getName().replace(".ser", "");
                Button button = new Button(filename);

                button.setStyle("-fx-background-color: #4d90fe; -fx-text-fill: #ffffff;");
                button.setPrefHeight(36);
                button.setPrefWidth(320);

                button.setOnAction(event -> {
                    SerializeUser su = new SerializeUser();
                    su.deserialize(filename);
                    loginButton.fire();
                });

                button.setLayoutX(242.0);
                button.setLayoutY(y);
                y += 50.0;
                anchorPane.getChildren().add(button);
            }
        }
    }
}
