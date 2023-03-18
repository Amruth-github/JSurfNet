package com.example.jsurfnet.controllers;

import com.example.jsurfnet.utils.TabsAndWv;
import com.example.jsurfnet.utils.ToolBar;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.jsurfnet.controllers.TabsController.readImage;
import static com.example.jsurfnet.controllers.TabsController.setIc;

public class ToolBarController implements Initializable {
    @FXML
    public Button backButton;
    @FXML
    public Button forwardButton;
    @FXML
    public Button reloadButton;
    @FXML
    public TextField urlField;
    @FXML
    public Button searchButton;
    @FXML
    public Button newTabButton;
    @FXML
    public Button newBookmarkButton;

    private TabPane tabPane;

    private WebEngine engine;

    BookmarksController bc = new BookmarksController();

    private void addBoookmark(){
        newBookmarkButton.setOnAction(event->{
            try {
                TabsAndWv TabsAndWvInstance;
                TabsAndWvInstance = TabsAndWv.getInstance();
                tabPane = TabsAndWvInstance.getTabPane();
                bc.addBookmark(tabPane.getSelectionModel().getSelectedItem().getText(), urlField.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @FXML
    private void handleBackButton() {
        TabsAndWv TabsAndWvInstance;
        TabsAndWvInstance = TabsAndWv.getInstance();
        engine = TabsAndWvInstance.getWebEngine();
        ObservableList<WebHistory.Entry> history = engine.getHistory().getEntries();
        int currentIndex = engine.getHistory().getCurrentIndex();

        if (currentIndex > 0) {
            engine.load(history.get(currentIndex - 1).getUrl());
        }
    }

    @FXML
    private void handleForwardButton() {
        TabsAndWv TabsAndWvInstance;
        TabsAndWvInstance = TabsAndWv.getInstance();
        engine = TabsAndWvInstance.getWebEngine();
        ObservableList<WebHistory.Entry> history = engine.getHistory().getEntries();
        int currentIndex = engine.getHistory().getCurrentIndex();
        System.out.println(history);
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
        TabsAndWv TabsAndWvInstance;
        TabsAndWvInstance = TabsAndWv.getInstance();
        tabPane = TabsAndWvInstance.getTabPane();
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            return (WebView) selectedTab.getContent();
        }
        return null;
    }

    public void loadURL(String url_from_bookmark, Tab tab) throws IOException {
        List<BufferedImage> img = readImage(url_from_bookmark);
        tab.setText(gethost(url_from_bookmark));
        if (img == null) {
            setIc(tab, true);
        }
        WebView webView = (WebView) tab.getContent();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url_from_bookmark);
    }

    static public  String gethost(String url) {
        URL u = null;
        try {
            u = new URL(url);
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        return u.getHost();
    }

    @FXML
    private void loadURL() throws MalformedURLException, IOException {
        String url = urlField.getText();
        if (!url.startsWith("http")) {
            url = "https://google.com/search?q=" + url;
        }
        TabsAndWv TabsAndWvInstance;
        TabsAndWvInstance = TabsAndWv.getInstance();
        tabPane = TabsAndWvInstance.getTabPane();
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        List<BufferedImage> img = readImage(url);
        selectedTab.setText(gethost(url));
        if (img == null) {
            setIc(selectedTab, true);
        }

        WebView webView = (WebView) selectedTab.getContent();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToolBar ToolBarInstance;
        ToolBarInstance = ToolBar.getInstance();
        ToolBarInstance.setBackButton(backButton);
        ToolBarInstance.setForwardButton(forwardButton);
        ToolBarInstance.setReloadButton(reloadButton);
        ToolBarInstance.setUrlField(urlField);
        ToolBarInstance.setSearchButton(searchButton);
        ToolBarInstance.setNewTabButton(newTabButton);
        ToolBarInstance.setNewBookmarkButton(newBookmarkButton);

        addBoookmark();
    }
}
