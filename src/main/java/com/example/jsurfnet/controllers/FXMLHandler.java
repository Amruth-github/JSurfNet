package com.example.jsurfnet.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLHandler implements Initializable {

    public AnchorPane TabAndWebView;
    public AnchorPane Bookmarks;
    public AnchorPane Toolbar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BrowserController object = new BrowserController();
        Pane bmview = object.getPage("Bookmarks");
        Pane tbview = object.getPage("ToolBar");
        Pane tabsview = object.getPage("Tabs");
        Bookmarks.getChildren().add(setAnchorDim(bmview));
        Toolbar.getChildren().add(setAnchorDim(tbview));
        TabAndWebView.getChildren().add(setAnchorDim(tabsview));

    }
    public Node setAnchorDim(Node view){
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        return view;
    }
}
