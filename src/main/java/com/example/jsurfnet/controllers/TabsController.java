package com.example.jsurfnet.controllers;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.example.jsurfnet.controllers.BookmarksController;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.net.MalformedURLException;
import java.awt.image.BufferedImage;
import net.sf.image4j.codec.ico.ICODecoder;
import java.util.List;
import javax.imageio.ImageIO;

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

    @FXML
    private Button newBookmarkButton;

    @FXML private Button backButton;
    @FXML private Button forwardButton;
    @FXML private Button reloadButton;
    @FXML private Button searchButton;

    private WebEngine engine;
    private Tab currentTab;

    BookmarksController bc = new BookmarksController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        ArrayList<Tab> savedTabs = new ArrayList<>();
        List<Tab> savedTabs = null;
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream("TabInfo.ser");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(fileIn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
//            savedTabs = (ArrayList) in.readObject();
            savedTabs = (List<Tab>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            addTab();
        }

        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fileIn.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Tab tab: savedTabs){
            tabPane.getTabs().add(tab);
        }
        addTab();
        addBoookmark();
    }

    private void addBoookmark(){
        newBookmarkButton.setOnAction(event->{
            try {
                bc.addBookmark(tabPane.getSelectionModel().getSelectedItem().getText(), urlField.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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

            FileOutputStream fileout = null;
            try {
                fileout = new FileOutputStream("TabInfo.ser");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(fileout);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                out.writeObject(new ArrayList<Tab>(tabPane.getTabs()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fileout.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Object info saved");

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
    private Tab setIc(Tab selectedTab) {
        Image i = new Image(new File("./icons/" + selectedTab.getText() + ".png").toURI().toString());
        ImageView iv = new ImageView();
        iv.setFitHeight(16);
        iv.setFitWidth(16);
        iv.setImage(i);
        selectedTab.setGraphic(iv);
        return selectedTab;
    }
    private Tab setIc(Tab selectedTab, boolean flag) {
        Image i = new Image(new File("./icons/favicon-standard.png").toURI().toString());
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

    static public List<BufferedImage> readImage(String u) throws IOException {

        List<BufferedImage> images = null;
        URL url = new URL(u);
        String path = "https://" + url.getHost() + "/favicon.ico";
        try {
            InputStream istr = new URL(path).openStream();
            images = ICODecoder.read(istr);
            ImageIO.write(images.get(0), "png", new File("./icons/" + url.getHost() + ".png"));
            return images;
        }
        catch (Exception e) {
            return null;
        }
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
