package com.example.jsurfnet.controllers;
import com.example.jsurfnet.utils.TabsAndWv;
import com.example.jsurfnet.utils.ToolBar;
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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.awt.image.BufferedImage;

import javafx.stage.FileChooser;
import net.sf.image4j.codec.ico.ICODecoder;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;

import com.example.jsurfnet.utils.TabSelection;

public class TabsController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private TabPane tabPane;

    private WebView webView;

    private WebEngine engine;
    private Tab currentTab;

    private final Button newTabButton;

    private TextField urlField;

    public TabsController(){
        ToolBar ToolBarInstance;
        ToolBarInstance = ToolBar.getInstance();
        newTabButton = ToolBarInstance.getNewTabButton();
        urlField = ToolBarInstance.getUrlField();
        System.out.println("Got new tab button");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TabsAndWv TabsAndWvInstance;
        TabsAndWvInstance = TabsAndWv.getInstance();
        TabsAndWvInstance.setWebView(webView);
        System.out.println("Set tab pane");

        setupListeners();
        addTab();
    }


    private void addTab(){
        Platform.runLater(newTabButton::fire);
    }

    private void setupListeners(){

        TabsAndWv TabsAndWvInstance;
        TabsAndWvInstance = TabsAndWv.getInstance();

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
            System.out.println("Ive reached here");
            System.out.println(tabPane);
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
            TabsAndWvInstance.setTabPane(tabPane);
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                WebView webView = (WebView) newTab.getContent();
                urlField.setText(getURL(webView));
                currentTab = newTab;
                engine = ((WebView) currentTab.getContent()).getEngine();
                TabsAndWvInstance.setWebEngine(engine);
                TabSelection x = new TabSelection(newTab);
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
    static public Tab setIc(Tab selectedTab) {
        Image i = new Image(new File("./icons/" + selectedTab.getText() + ".png").toURI().toString());
        ImageView iv = new ImageView();
        iv.setFitHeight(16);
        iv.setFitWidth(16);
        iv.setImage(i);
        selectedTab.setGraphic(iv);
        return selectedTab;
    }
    static public Tab setIc(Tab selectedTab, boolean flag) {
        Image i = new Image(new File("./icons/favicon-standard.png").toURI().toString());
        ImageView iv = new ImageView();
        iv.setFitHeight(16);
        iv.setFitWidth(16);
        iv.setImage(i);
        selectedTab.setGraphic(iv);
        return selectedTab;
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



    public String getURL(WebView webView) {
        return webView.getEngine().getLocation();
    }
}
