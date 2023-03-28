package com.example.jsurfnet.controllers;
import com.example.jsurfnet.utils.*;
import com.example.jsurfnet.utils.Icon;
import com.example.jsurfnet.utils.ToolBar;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.ResourceBundle;
import java.net.MalformedURLException;
import java.io.File;
import com.example.jsurfnet.WebBrowser;
import javafx.stage.Popup;

import javax.swing.*;


public class TabsController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private TabPane tabPane;

    private WebEngine engine;
    private Tab currentTab;

    private final Button newTabButton;

    private TextField urlField;

    private ImageView iv = new ImageView(new Image(new File("./icons/spinner.gif").toURI().toString()));

    private PasswordManager pwm = null;

    public TabsController(){
        ToolBar ToolBarInstance;
        ToolBarInstance = ToolBar.getInstance();
        newTabButton = ToolBarInstance.getNewTabButton();
        urlField = ToolBarInstance.getUrlField();
        try {
            pwm = PasswordManager.getUserPassword();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            WebEngine webEngine = newWebView.getEngine();
            webEngine.load("https://www.google.com");
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
                            PasswordPopup pp = null;
                            if (pwm.exists(urlField.getText())) {
                                pp = new PasswordPopup(false);
                                pp.setPassword(pwm.getCreds(urlField.getText()).getPassword());
                                pp.setUsername(pwm.getCreds(urlField.getText()).getUsername());
                                pp.show(WebBrowser.getScene().getWindow(), WebBrowser.getScene().getWidth() - pp.getWidth() - 10, 100);
                            } else {
                                pp = new PasswordPopup(true);
                                pp.show(WebBrowser.getScene().getWindow(), WebBrowser.getScene().getWidth() - pp.getWidth() - 10, 100);
                            }
                            PasswordPopup finalPp = pp;
                            pp.getSaveButton().setOnAction(actionEvent -> {
                                pwm.addCreds(urlField.getText(), finalPp.getUsername(), finalPp.getPassword());
                                finalPp.hide();
                                JOptionPane.showMessageDialog(null, "Saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            });
                        }

                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }));

            newWebView.setPrefSize(800, 600);
            tab.setContent(newWebView);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);


            System.out.println(tabPane.getTabs());
            TabsAndWvInstance.setTabPane(tabPane);



            webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {

                urlField.setText(newValue);
                tabPane.getSelectionModel().getSelectedItem().setText(gethost(newValue));
                TabsAndWvInstance.setTabPane(tabPane);
                //tabPane.getSelectionModel().getSelectedItem().setGraphic(new Icon(urlField.getText()).getImage());
            });
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                WebView webView = (WebView) newTab.getContent();
                urlField.setText(getURL(webView));
                currentTab = newTab;
                engine = ((WebView) currentTab.getContent()).getEngine();
                System.out.println(engine);
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
    public String getURL(WebView webView) {
        return webView.getEngine().getLocation();
    }
}
