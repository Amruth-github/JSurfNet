package com.example.jsurfnet.controllers;
import com.example.jsurfnet.utils.Icon;
import com.example.jsurfnet.utils.TabsAndWv;
import com.example.jsurfnet.utils.ToolBar;
import com.example.jsurfnet.utils.webHistory;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

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
    @FXML
    public Button logoutButton;
    @FXML
    public Button showPassword;

    @FXML
    public Button homeButton;
    public Button showHistory;

    private TabPane tabPane;

    private WebEngine engine;

    private int curr_index;

    private webHistory history = null;

    BookmarksController bc = new BookmarksController();


    private void addBoookmark(){
        newBookmarkButton.setOnAction(event->{
            try {
                TabsAndWv TabsAndWvInstance;
                TabsAndWvInstance = TabsAndWv.getInstance();
                tabPane = TabsAndWvInstance.getTabPane();
                bc.addBookmark(tabPane.getSelectionModel().getSelectedItem().getText(), urlField.getText(), true);
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
        WebHistory webHistory;
        webHistory = engine.getHistory();
        int currentIndex = engine.getHistory().getCurrentIndex();

        if (currentIndex > 0) {
            webHistory.go(-1);
        }
    }

    @FXML
    private void handleForwardButton() {
        TabsAndWv TabsAndWvInstance;
        TabsAndWvInstance = TabsAndWv.getInstance();
        engine = TabsAndWvInstance.getWebEngine();
        //ObservableList<WebHistory.Entry> history = engine.getHistory().getEntries();
        //int currentIndex = engine.getHistory().getCurrentIndex();
        //System.out.println(history);
        //if (currentIndex < history.size() - 1) {
            //engine.load(history.get(currentIndex + 1).getUrl());
        //}
        WebHistory webHistory;
        webHistory = engine.getHistory();
        int currentIndex = webHistory.getCurrentIndex();
        int maxIndex = webHistory.getEntries().size() - 1;
        if (currentIndex < maxIndex) {
            // Go forward
            webHistory.go(1);
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
        tab.setText(gethost(url_from_bookmark));
        tab.setGraphic(new Icon(url_from_bookmark).getImage());
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
        selectedTab.setText(gethost(url));

        WebView webView = (WebView) selectedTab.getContent();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url);
    }


//    public void setLogoutListener(Consumer<ActionEvent> logoutListener) throws IOException {
//        logoutButton.setOnAction(logoutListener::accept);
//    }

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
        ToolBarInstance.setLogoutButton(logoutButton);
        ToolBarInstance.setShowPassword(showPassword);
        ToolBarInstance.setHomeButton(homeButton);
        ToolBarInstance.setHistoryButton(showHistory);
        ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/icons/history.png")));
        iv.setFitWidth(16);
        iv.setFitHeight(16);
        showHistory.setGraphic(iv);
        showPassword.setVisible(false);
        try {
            history = webHistory.getUserHistory();
            ToolBarInstance.setHistory(history);
            System.out.println(history.getList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        showHistory.setOnAction(actionEvent -> {
            new webHistoryView().render();
        });

        urlField.setOnMouseClicked(actionEvent -> {
            urlField.selectAll();
        });
        addBoookmark();
    }

    public void goHome() {
        TabsAndWv instance = TabsAndWv.getInstance();
        engine = instance.getWebEngine();
        engine.load("https://www.google.co.in/");
    }
}
