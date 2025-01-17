
package com.example.jsurfnet.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import com.example.jsurfnet.singleton.CurrentUser;
import com.example.jsurfnet.services.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.bson.Document;

import javax.swing.JOptionPane;

public class LoginController implements Initializable {

    @FXML
    public Button signupButton;
    @FXML
    public Button guestButton;
    @FXML
    public AnchorPane anchorPane;

    @FXML
    public ScrollPane scrollPane;
    @FXML
    public VBox ProfileHolder;
    public Label exisitngProfile;

    public Label or;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public Button loginButton;

    private MongoCollection<Document> usersCollection;

    public LoginController() {
        MongoDatabase database = MongoDriver.getMongo();
        this.usersCollection = database.getCollection("users");
    }

    public boolean authenticateUser() throws IOException {
        // clicking profile button is enough to login
        if (CurrentUser.getInstance().getUsername()!= null) {
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
                    JOptionPane.showMessageDialog(null, "Password has been changed. Please login instead.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            return true;
        }

        // Login button is clicked manually after entering username and passwords

        Document user = usersCollection.find(new Document("username", usernameField.getText())).first();
        if (user != null) {
            if (passwordField.getText().strip().equals(user.getString("password"))){
                CurrentUser currentUser = CurrentUser.getInstance();
                //set Username means set username and password both
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
            Document for_history = new Document();
            for_history.append("user", usernameField.getText().strip());
            for_history.append("history", new webHistory().getSerialized());
            MongoDriver.getMongo().getCollection("history").insertOne(for_history);
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
        if (!new File("userprofiles").exists()) {
            new File("userprofiles").mkdir();
        }
        File dir = new File("userprofiles");
        boolean showUI = Objects.requireNonNull(new File("userprofiles").list()).length != 0;
        or.setVisible(showUI);
        exisitngProfile.setVisible(showUI);
        scrollPane.setVisible(showUI);


        double y = 100.0;

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile() && file.getName().endsWith(".ser")) {
                String filename = file.getName().replace(".ser", "");
                Button button = new Button(filename);

                button.setStyle("-fx-background-color: #ADD8E6; -fx-text-fill: #333333; -fx-border-width: 1px; -fx-border-color: #b3b3b3");
                button.setPrefHeight(36);
                button.setPrefWidth(320);

                button.setOnAction(event -> {
                    SerializeUser su = new SerializeUser();
                    su.deserialize(filename); // also sets the currentuser instance
                    loginButton.fire();
                });

                button.setLayoutX(242.0);
                button.setLayoutY(y);
                y += 50.0;
                ProfileHolder.setSpacing(8);
                ProfileHolder.getChildren().addAll(button);

                ContextMenu cm = new ContextMenu();
                MenuItem m1 = new MenuItem("Delete Profile");
                cm.getItems().add(m1);
                m1.setOnAction(actionEvent -> {
                    if (file.delete() == true) {
                        ProfileHolder.getChildren().remove(button);
                    }
                });
                button.setContextMenu(cm);
            }
        }
    }
}
