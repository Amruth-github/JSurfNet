package com.example.jsurfnet.controllers;

import com.example.jsurfnet.utils.History;
import com.example.jsurfnet.utils.TabsAndWv;
import com.example.jsurfnet.utils.ToolBar;
import com.example.jsurfnet.utils.webHistory;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class webHistoryView {

    private static Tab historyTab = null;

    private static boolean shouldAdd = true;
    public void render() {
        if (historyTab == null) {
            historyTab = new Tab();
        }
        historyTab.setText("History");

        // Create a VBox to hold the list of history items and clear history button
        VBox historyList = new VBox();
        historyList.setAlignment(Pos.TOP_LEFT);
        historyList.setSpacing(10);
        historyList.setPadding(new Insets(10));
        historyList.setStyle("-fx-background-color: #1C1C1C;");

        // Create a ScrollPane to display the history list
        ScrollPane scrollPane = new ScrollPane(historyList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #1C1C1C;");

        // Load the user's history from the database
        webHistory userHistory = ToolBar.getInstance().getHistory();
        List<History> historyItems = userHistory.getList();

        // Create a TableView to display the history items in a tabular format
        TableView<History> historyTable = new TableView<>();
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        historyTable.setPrefHeight(400);
        historyTable.setStyle("-fx-font-size: 14; -fx-font-family: 'Arial'; -fx-background-color: #1C1C1C; -fx-text-fill: white;");
        historyTable.setPlaceholder(new Label("No History!"));

        TableColumn<History, String> urlColumn = new TableColumn<>("URL");
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.setPrefWidth(600);
        urlColumn.setCellFactory(column -> {
            TableCell<History, String> cell = new TableCell<History, String>() {
                @Override
                protected  void updateItem(String url, boolean empty) {
                    super.updateItem(url, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(url);
                    }
                }
            };
            cell.setOnMouseClicked((event) -> {
                if (!(cell.isEmpty())) {
                    Tab newTab = new Tab(TabsController.gethost(cell.getText()));
                    WebView webView = new WebView();
                    WebEngine webEngine = webView.getEngine();
                    new TabsController().ListnersForWebView(newTab, webEngine);
                    webEngine.load(cell.getText());
                    newTab.setContent(webView);
                    TabsAndWv.getInstance().getTabPane().getTabs().add(newTab);
                    TabsAndWv.getInstance().getTabPane().getSelectionModel().select(newTab);
                    ToolBar.getInstance().getUrlField().setText(cell.getText());

                }
            });
            return cell;
        });

        TableColumn<History, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeColumn.setPrefWidth(200);

        historyTable.getColumns().addAll(urlColumn, timeColumn);
        historyTable.getItems().addAll(historyItems);

        // Add the TableView to the VBox
        historyList.getChildren().add(historyTable);

        // Create a button to clear the history
        Button clearButton = new Button("Clear History");
        clearButton.setStyle("-fx-background-color: #63D471; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold;");
        clearButton.setOnAction(event -> {
            ToolBar.getInstance().getHistory().clear();
            new webHistoryView().render();
        });

        // Add the clear button to the VBox
        historyList.getChildren().add(clearButton);

        // Set the content of the history tab to the ScrollPane
        historyTab.setContent(scrollPane);

        // Add the history tab to the TabPane
        if (shouldAdd) {
            TabsAndWv.getInstance().getTabPane().getTabs().add(historyTab);
            shouldAdd = false;
        }

        TabsAndWv.getInstance().getTabPane().getSelectionModel().select(historyTab);
    }
}
