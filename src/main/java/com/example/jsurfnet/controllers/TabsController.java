package com.example.jsurfnet.controllers;
<<<<<<< Updated upstream
=======
import com.example.jsurfnet.WebBrowser;
import com.example.jsurfnet.utils.*;
import com.example.jsurfnet.utils.ToolBar;
>>>>>>> Stashed changes
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
<<<<<<< Updated upstream
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.IOException;
=======
import javafx.stage.Screen;

>>>>>>> Stashed changes
import java.net.URL;
import java.util.ResourceBundle;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.awt.image.BufferedImage;
import net.sf.image4j.codec.ico.ICODecoder;

import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.io.File;

import com.example.jsurfnet.utils.TabSelection;

public class TabsController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private TextField urlField;

    @FXML
    private TabPane tabPane;

    @FXML
    private Button newTabButton;

    @FXML
    private WebView webView;

    @FXML private Button backButton;
    @FXML private Button forwardButton;
    @FXML private Button reloadButton;
    @FXML private Button searchButton;

    private WebEngine engine;
    private Tab currentTab;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTab();
    }

    private void addTab(){
        Platform.runLater(() -> {
            newTabButton.fire();
        });

        newTabButton.setOnAction(event -> {

            Tab tab = new Tab(gethost("https://www.google.com"));
            WebView newWebView = new WebView();
            try {
                readImage("https://www.google.com");
                setIc(tab);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            WebEngine webEngine = newWebView.getEngine();
            webEngine.load("https://www.google.com");
<<<<<<< Updated upstream
=======
            iv.setFitHeight(16);
            iv.setFitWidth(16);
            tab.setGraphic(iv);

            webEngine.getLoadWorker().stateProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.RUNNING) {
                    tab.setGraphic(iv);
                } else {
                    tab.setGraphic(new Icon(urlField.getText()).getImage());
                }
                if (newValue == Worker.State.SUCCEEDED) {
                    try {
                        boolean hasField = (boolean) webEngine.executeScript("function checkFeilds() {" +
                                "    var fields = document.querySelectorAll('input');" +
                                "    for (let i = 0; i < fields.length; i++) {" +
                                "        if (fields[i].type == 'password' || fields[i].name == 'password' || fields[i].name == 'username') {" +
                                "            return true;" +
                                "        }" +
                                "    }" +
                                "    return false;" +
                                "}" +
                                "" +
                                "checkFeilds()");
                        if (hasField) {
                            pwm.addCreds("https://www.pesuacademy.com", "PES1UG20CS038", "OIEHF");
                            if (pwm.exists(urlField.getText())) {
                                PasswordPopup pp = new PasswordPopup(false);
                                pp.setPassword(pwm.getCreds(urlField.getText()).getPassword());
                                pp.setUsername(pwm.getCreds(urlField.getText()).getUsername());
                                pp.show(WebBrowser.getScene().getWindow(), WebBrowser.getScene().getWidth() - pp.getWidth() - 10, 100);
                            } else {
                                PasswordPopup pp = new PasswordPopup(true);
                                pp.show(WebBrowser.getScene().getWindow(), WebBrowser.getScene().getWidth() - pp.getWidth() - 10, 100);
                            }
                        }
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }));

>>>>>>> Stashed changes
            newWebView.setPrefSize(800, 600);
            tab.setContent(newWebView);

            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);

            webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
                urlField.setText(newValue);
                tabPane.getSelectionModel().getSelectedItem().setText(gethost(newValue));
                try {
                    List<BufferedImage> img = readImage(newValue);
                    if (img != null) {
                        setIc(tabPane.getSelectionModel().getSelectedItem());
                    }
                    else {
                        setIc(tabPane.getSelectionModel().getSelectedItem(), true);
                    }
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
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
                TabSelection x = new TabSelection(newTab);
                System.out.println("Set the tab");
            }
        });
    }

    private String gethost(String url) {
        URL u = null;
        try {
            u = new URL(url);
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        return u.getHost();
    }
    private Tab setIc(Tab selectedTab) {
        Image i = new Image(new File("favicon.png").toURI().toString());
        ImageView iv = new ImageView();
        iv.setFitHeight(16);
        iv.setFitWidth(16);
        iv.setImage(i);
        selectedTab.setGraphic(iv);
        return selectedTab;
    }
    private Tab setIc(Tab selectedTab, boolean flag) {
        Image i = new Image(new File("favicon-standard.png").toURI().toString());
        ImageView iv = new ImageView();
        iv.setFitHeight(16);
        iv.setFitWidth(16);
        iv.setImage(i);
        selectedTab.setGraphic(iv);
        return selectedTab;
    }
    
    @FXML
    private void loadURL() throws MalformedURLException, IOException {
        String url = urlField.getText();
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

    private List<BufferedImage> readImage(String u) throws IOException {

        List<BufferedImage> images = null;
        URL url = new URL(u);
        String path = "https://" + url.getHost() + "/favicon.ico";
        try {
            InputStream istr = new URL(path).openStream();
            images = ICODecoder.read(istr);
            ImageIO.write(images.get(0), "png", new File("favicon.png"));
            return images;
        }
        catch (Exception e) {
            return null;
        }
    }

    public void loadURL(String url_from_bookmark, Tab tab){
        WebView webView = (WebView) tab.getContent();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url_from_bookmark);
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
