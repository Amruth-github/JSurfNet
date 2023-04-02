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

    private static boolean hasField = false;

    private PasswordPopup pp = null;

    private Button showPassword = ToolBar.getInstance().getShowPassword();

    public TabsController(){
        ToolBar ToolBarInstance;
        ToolBarInstance = ToolBar.getInstance();
        newTabButton = ToolBarInstance.getNewTabButton();
        urlField = ToolBarInstance.getUrlField();
        try {
            pwm = PasswordManager.getUserPassword();
        } catch (Exception e) {
            pwm = null;
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

    private void initpopup() {
        if (pwm.exists(urlField.getText())) {
            pp = new PasswordPopup(false);
            pp.setPassword(pwm.getCreds(urlField.getText()).getPassword());
            pp.setUsername(pwm.getCreds(urlField.getText()).getUsername());
            pp.show(WebBrowser.getScene().getWindow(), WebBrowser.getScene().getWidth() - pp.getWidth() - 10, 100);
        } else {
            pp = new PasswordPopup(true);
            pp.show(WebBrowser.getScene().getWindow(), WebBrowser.getScene().getWidth() - pp.getWidth() - 10, 100);
        }
    }

    private void setupListeners(){

        TabsAndWv TabsAndWvInstance;
        TabsAndWvInstance = TabsAndWv.getInstance();

        showPassword.setOnAction(actionEvent -> {
            initpopup();
        });


        newTabButton.setOnAction(event -> {
            Tab tab = new Tab(gethost("https://www.google.com"));
            WebView newWebView = new WebView();
            WebEngine webEngine = newWebView.getEngine();
            webEngine.load("https://www.google.com");
            iv.setFitHeight(16);
            iv.setFitWidth(16);
            tab.setGraphic(iv);

            webEngine.getLoadWorker().stateProperty().addListener(((observable, oldValue, newValue) -> {
                showPassword.setVisible(false);
                if (pp != null) {
                    pp.hide();
                }
                if (newValue == Worker.State.FAILED || newValue == Worker.State.CANCELLED) {
                    webEngine.loadContent("<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "\t<title>Offline</title>\n" +
                            "\t<style>\n" +
                            "\t\tbody {\n" +
                            "\t\t\tbackground-color: #f8f8f8;\n" +
                            "\t\t\tfont-family: Arial, sans-serif;\n" +
                            "\t\t\ttext-align: center;\n" +
                            "\t\t}\n" +
                            "\n" +
                            "\t\th1 {\n" +
                            "\t\t\tcolor: #333;\n" +
                            "\t\t\tfont-size: 2.5em;\n" +
                            "\t\t\tmargin-top: 40px;\n" +
                            "\t\t\tmargin-bottom: 20px;\n" +
                            "\t\t\tletter-spacing: 2px;\n" +
                            "\t\t}\n" +
                            "\n" +
                            "\t\tp {\n" +
                            "\t\t\tcolor: #666;\n" +
                            "\t\t\tfont-size: 1.2em;\n" +
                            "\t\t\tmargin-bottom: 40px;\n" +
                            "\t\t\tline-height: 1.5;\n" +
                            "\t\t}\n" +
                            "\n" +
                            "\t\timg {\n" +
                            "\t\t\tmax-width: 100%;\n" +
                            "\t\t\theight: auto;\n" +
                            "\t\t}\n" +
                            "\n" +
                            "\t\t@keyframes slide-in {\n" +
                            "\t\t\tfrom {\n" +
                            "\t\t\t\topacity: 0;\n" +
                            "\t\t\t\ttransform: translateX(-50%);\n" +
                            "\t\t\t}\n" +
                            "\t\t\tto {\n" +
                            "\t\t\t\topacity: 1;\n" +
                            "\t\t\t\ttransform: translateX(0);\n" +
                            "\t\t\t}\n" +
                            "\t\t}\n" +
                            "\n" +
                            "\t\t.offline-icon {\n" +
                            "\t\t\tanimation: slide-in 1s ease-out;\n" +
                            "\t\t}\n" +
                            "\t</style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "\t<h1>Oops! You're Offline</h1>\n" +
                            "\t<img class='offline-icon' src=\"" + new File("./src/main/resources/html/no.png").toURI() +  "\" alt=\"Offline Icon\">\n" +
                            "\t<p>Sorry, it looks like you're not connected to the internet. Please check your connection and try again.</p>\n" +
                            "</body>\n" +
                            "</html>\n");
                }
                if (newValue == Worker.State.RUNNING) {
                    tab.setGraphic(iv);
                } else {
                    tab.setGraphic(new Icon(urlField.getText()).getImage());
                }
                if (newValue == Worker.State.SUCCEEDED) {
                    try {
                        hasField = (boolean) webEngine.executeScript("function checkFeilds() {" +
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
                        if (pwm != null && hasField) {
                            showPassword.setVisible(true);
                            initpopup();
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
