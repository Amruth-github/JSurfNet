package com.example.jsurfnet.controllers;

import com.example.jsurfnet.utils.History;
import com.example.jsurfnet.utils.webHistory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class webHistoryController implements Initializable {
    @FXML
    private TableView<History> historyTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<History, LocalDate> dateColumn = new TableColumn<>("URL");
        TableColumn<History, String> urlColumn = new TableColumn<>("Date");

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));

        historyTable.getColumns().add(urlColumn);
        historyTable.getColumns().add(dateColumn);

        // Load browsing history from webHistory object
        try {
            webHistory wh = webHistory.getUserHistory();
            System.out.println(wh.getList());
            ObservableList<History> observableHistory = FXCollections.observableArrayList(wh.getList());
            historyTable.setItems(observableHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}