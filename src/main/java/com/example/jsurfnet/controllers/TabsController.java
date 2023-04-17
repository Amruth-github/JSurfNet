package com.example.jsurfnet.controllers;
import com.example.jsurfnet.singleton.TabSelection;
import com.example.jsurfnet.singleton.TabsAndWv;
import com.example.jsurfnet.services.*;
import com.example.jsurfnet.services.Icon;
import com.example.jsurfnet.singleton.ToolBar;
import com.example.jsurfnet.views.PasswordPopup;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.net.MalformedURLException;
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

    private ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/icons/spinner.gif")));

    private static PasswordManager pwm = null;

    private PasswordPopup pp = null;
    private Button showPassword = ToolBar.getInstance().getShowPassword();

    public TabsController(){
        ToolBar ToolBarInstance;
        ToolBarInstance = ToolBar.getInstance();
        newTabButton = ToolBarInstance.getNewTabButton();
        iv.setFitHeight(16);
        iv.setFitWidth(16);
        urlField = ToolBarInstance.getUrlField();
        try {
            pwm = PasswordManager.getUserPassword();
            TabsAndWv.setPasswordManager(pwm);
        } catch (Exception e) {
            pwm = null;
        }
    }

    public void ListnersForWebView(Tab tab, WebEngine webEngine) {
        webEngine.getLoadWorker().stateProperty().addListener(((observable, oldValue, newValue) -> {
            showPassword.setVisible(false);
            if (pp != null) {
                pp.hide();
            }
            if (newValue == Worker.State.FAILED || newValue == Worker.State.CANCELLED) {
                webEngine.load(getClass().getResource("/html/nointernet.html").toExternalForm());
            }
            if (newValue == Worker.State.RUNNING) {
                tab.setGraphic(iv);
            } else {
                tab.setGraphic(new Icon(urlField.getText()).getImage());
            }
            if (newValue == Worker.State.SUCCEEDED) {
                new Thread(() -> {
                    ToolBar.getInstance().getHistory().appendHistory(urlField.getText());
                }).start();
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupListeners();
        addTab();
        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
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

        tabPane.setOnKeyPressed(actionEvent -> {
            if (actionEvent.isControlDown() && actionEvent.getCode() == KeyCode.W) {
                tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedItem());
            }
            if (actionEvent.isControlDown() && actionEvent.getCode() == KeyCode.T) {
                newTabButton.fire();
            }
            if (actionEvent.isControlDown() && actionEvent.getCode() == KeyCode.TAB) {
                tabPane.getSelectionModel().select((tabPane.getSelectionModel().getSelectedIndex()) % (tabPane.getTabs().size()));
            }
            if (actionEvent.isControlDown() && actionEvent.getCode() == KeyCode.R) {
                ToolBar.getInstance().getReloadButton().fire();
            }
            if (actionEvent.isControlDown() && actionEvent.getCode() == KeyCode.H) {
                ToolBar.getInstance().getShowHistory().fire();
            }
            if (actionEvent.isControlDown() && actionEvent.getCode() == KeyCode.P) {
                try {
                    Objects.requireNonNull(TableFactory.getTable("PasswordTable")).render();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        showPassword.setOnAction(actionEvent -> {
            initpopup();
        });

        newTabButton.setOnAction(event -> {
            Tab tab = new Tab(gethost("https://www.google.com"));
            WebView newWebView = new WebView();
            WebEngine webEngine = newWebView.getEngine();
            webEngine.load("https://www.google.com");
            tab.setGraphic(iv);

            ListnersForWebView(tab, webEngine);

            newWebView.setPrefSize(800, 600);
            tab.setContent(newWebView);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            TabsAndWvInstance.setTabPane(tabPane);



            webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {

                urlField.setText(newValue);
                tabPane.getSelectionModel().getSelectedItem().setText(gethost(newValue));
                TabsAndWvInstance.setTabPane(tabPane);
                //tabPane.getSelectionModel().getSelectedItem().setGraphic(new Icon(urlField.getText()).getImage());
            });
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null && !newTab.getText().equals("History") && !newTab.getText().equals("Passwords")) {
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
    public String getURL(WebView webView) {
        return webView.getEngine().getLocation();
    }
}
