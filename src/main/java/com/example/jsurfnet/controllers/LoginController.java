
package com.example.jsurfnet.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    public LoginController(){
    }

    public void setLoginListener(Consumer<ActionEvent> loginListener) {
        loginButton.setOnAction(loginListener::accept);
    }

    public boolean authenticateUser() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("JSurNet");
        MongoCollection<Document> usersCollection = database.getCollection("users");
        Document document = new Document();
        document.append("username", usernameField.getText());
        System.out.println("here");
        document.append("password", passwordField.getText());
        usersCollection.insertOne(document);
        mongoClient.close();
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
