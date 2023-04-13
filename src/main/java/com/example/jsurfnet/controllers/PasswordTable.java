package com.example.jsurfnet.controllers;

import com.example.jsurfnet.utils.Credential;
import com.example.jsurfnet.utils.PasswordManager;
import com.example.jsurfnet.utils.TabsAndWv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;

public class PasswordTable {

    private HashMap<String, Credential> urlToPassword = TabsAndWv.getInstance().getPasswordManager().getHash();
    private Tab passwordTab;

    public void render() {
        if (passwordTab == null) {
            passwordTab = new Tab();
            passwordTab.setText("Passwords");
        }

    TableView<Credential> tableView = new TableView<>();
    TableColumn<Credential, String> urlColumn = new TableColumn<>("URL");
    urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
    TableColumn<Credential, String> usernameColumn = new TableColumn<>("Username");
    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    TableColumn<Credential, String> passwordColumn = new TableColumn<>("Password");
    passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
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

    TabsAndWv.getInstance().getTabPane().getSelectionModel().select(passwordTab);

    }
}