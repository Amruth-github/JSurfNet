
package com.example.jsurfnet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;

import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;


import javax.swing.*;

public class LoginController implements Initializable {

    @FXML
    public Button signupButton;
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
        this.mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("JSurNet");
        this.usersCollection = database.getCollection("users");
    }

    public void setLoginListener(Consumer<ActionEvent> loginListener) {
        loginButton.setOnAction(loginListener::accept);
    }

    public void setSignupListener(Consumer<ActionEvent> signupListener) {
        signupButton.setOnAction(signupListener::accept);
    }


    public boolean authenticateUser() {
        return true;
    }

    public boolean signup() {
        Document document = new Document();
        Document existingUser = usersCollection.find(new Document("username", usernameField.getText())).first();
        if (existingUser != null) {
            JOptionPane.showMessageDialog(null, "User already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            document.append("username", usernameField.getText());
            document.append("password", passwordField.getText());
            usersCollection.insertOne(document);
            return true;
        }
//        mongoClient.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Button logoutButton = new Button("Logout");
//        logoutButton.setOnAction(logoutListener);
//        toolBar.getItems().add(logoutButton);
    }
}
