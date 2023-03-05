package com.example.jsurfnet.controllers;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.SimpleTimeZone;

public class TabsController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private TextField urlField;

    @FXML
    private TabPane tabPane;

    @FXML
    private Button newTabButton;

    @FXML private Button backButton;
    @FXML private Button forwardButton;
    @FXML private Button reloadButton;
    @FXML private Button searchButton;

    private WebEngine engine;
    private Tab currentTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        newTabButton.setOnAction(event -> {

            Tab tab = new Tab("New Tab");
            WebView newWebView = new WebView();
            WebEngine webEngine = newWebView.getEngine();
            webEngine.load("https://www.google.com/");
            newWebView.setPrefSize(800, 600);
            tab.setContent(newWebView);

            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            WebHistory history = engine.getHistory();

            history.getEntries().addListener((ListChangeListener<WebHistory.Entry>) change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        WebHistory.Entry entry = change.getAddedSubList().get(0);
                        urlField.setText(entry.getUrl());
                    }
                }
            });
        });

        engine = new WebView().getEngine();

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                WebView webView = (WebView) newTab.getContent();
                urlField.setText(getURL(webView));
                currentTab = newTab;
                engine = ((WebView) currentTab.getContent()).getEngine();
            }
        });


    }

    @FXML
    private void loadURL() {
        String url = urlField.getText();
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        WebView webView = (WebView) selectedTab.getContent();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url);
    }

    @FXML
    private void handleBackButton() {
        ObservableList<WebHistory.Entry> history = engine.getHistory().getEntries();
        int currentIndex = engine.getHistory().getCurrentIndex();

        if (currentIndex > 0) {
            engine.load(history.get(currentIndex - 1).getUrl());
        }
    }

    @FXML
    private void handleForwardButton() {
        ObservableList<WebHistory.Entry> history = engine.getHistory().getEntries();
        int currentIndex = engine.getHistory().getCurrentIndex();

        if (currentIndex < history.size() - 1) {
            engine.load(history.get(currentIndex + 1).getUrl());
        }
    }

    @FXML
    private void handleReloadButton() {
        WebView webView = getSelectedWebView();
        if (webView != null) {
            WebEngine webEngine = webView.getEngine();
            webEngine.reload();
        }
    }

    private WebView getSelectedWebView() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            return (WebView) selectedTab.getContent();
        }
        return null;
    }

    public String getURL(WebView webView) {
        return webView.getEngine().getLocation();
    }
}
