package com.example.jsurfnet.Prototype;

import com.example.jsurfnet.controllers.TabsController;
import com.example.jsurfnet.services.IconBuilder;
import javafx.concurrent.Worker;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class TabImpl implements TabPrototype {

    private final Tab tab;

    public TabImpl(Tab tab) {
        this.tab = tab;
    }

    @Override
    public Tab clone() {
        Tab newTab = new Tab();
        newTab.setText(tab.getText());
        newTab.setClosable(tab.isClosable());
        newTab.setUserData(tab.getUserData());
        newTab.setGraphic(new IconBuilder("/icons/spinner.gif", 16, 16).getIcon());

        // get old web view and engine
        WebView originalWebView = (WebView) tab.getContent();
        WebEngine originalWebEngine = originalWebView.getEngine();

        // create new web view and engine
        WebView newWebView = new WebView();
        WebEngine newWebEngine = newWebView.getEngine();

        newWebEngine.load(originalWebEngine.getLocation());

        newTab.setContent(newWebView);

        new TabsController().ListnersForWebView(newTab, newWebEngine);

        return newTab;
    }
}
