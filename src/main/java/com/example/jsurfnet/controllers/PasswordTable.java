package com.example.jsurfnet.controllers;

import com.example.jsurfnet.utils.Credential;
import com.example.jsurfnet.utils.Icon;
import com.example.jsurfnet.utils.PasswordManager;
import com.example.jsurfnet.utils.TabsAndWv;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;

import java.util.HashMap;

public class PasswordTable {

    private PasswordManager passwordManager = TabsAndWv.getInstance().getPasswordManager();

    private HashMap<String, Credential> urlToPassword = TabsAndWv.getInstance().getPasswordManager().getHash();
    private static Tab passwordTab;

    public PasswordTable() throws Exception {
    }

    public void render() {
        if (passwordTab == null) {
            passwordTab = new Tab();
            passwordTab.setText("Passwords");
            ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/icons/padlock.png")));
            iv.setFitWidth(16);
            iv.setFitHeight(16);
            passwordTab.setGraphic(iv);
        }

        TableView<Credential> tableView = new TableView<>();
        TableColumn<Credential, String> urlColumn = new TableColumn<>("URL");
        urlColumn.setCellValueFactory(data -> {
            String url = null;
            for (String key : urlToPassword.keySet()) {
                if (urlToPassword.get(key).equals(data.getValue())) {
                    url = key;
                    break;
                }
            }
            return new SimpleStringProperty(url);
        });
        urlColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item);
                    setGraphic(new Icon(item).getImage());
                }
            }
        });

        TableColumn<Credential, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Credential, String> passwordColumn = new TableColumn<>("Password");

        passwordColumn.setPrefWidth(100);
        passwordColumn.setCellFactory(column -> new TableCell<>() {
            final Button showButton = new Button();
            boolean show = false;

            {
                ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/icons/show.png")));
                iv.setFitHeight(16);
                iv.setFitWidth(16);
                showButton.setGraphic(iv);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                TextField passwordField = new TextField();
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox();
                    passwordField.setText("***");
                    hBox.getChildren().addAll(passwordField, showButton);
                    setGraphic(hBox);
                }
                showButton.setOnAction(actionEvent -> {
                    if (!show) {
                        show = true;
                        passwordField.setText(passwordManager.getHash().get(urlColumn.getCellData(getIndex())).getPassword());
                    } else {
                        show = false;
                        passwordField.setText("***");
                    }
                });
                passwordField.setOnAction(e -> {
                    String url = urlColumn.getCellData(getIndex());
                    if (!passwordField.getText().equals("***")) {
                        passwordManager.updateCreds(url, passwordManager.getHash().get(url).getUsername(), passwordField.getText());
                    }
                    passwordField.setText("***");
                    try {
                        new PasswordTable().render();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                });
                setPrefWidth(100);
            }
        });

        tableView.getColumns().addAll(urlColumn, usernameColumn, passwordColumn);

        ObservableList<Credential> credentialList = FXCollections.observableArrayList();
        for (Credential credential : urlToPassword.values()) {
            credentialList.add(credential);
        }
        tableView.setItems(credentialList);

        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        passwordTab.setContent(scrollPane);

        if (!TabsAndWv.getInstance().getTabPane().getTabs().contains(passwordTab)) {
            TabsAndWv.getInstance().getTabPane().getTabs().add(passwordTab);
        }
        TabsAndWv.getInstance().getTabPane().getSelectionModel().select(passwordTab);
    }
}