package com.example.jsurfnet.Prototype;

import javafx.concurrent.Worker;
import javafx.scene.control.Tab;
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

        // get old web view and engine
        WebView originalWebView = (WebView) tab.getContent();
        WebEngine originalWebEngine = originalWebView.getEngine();

        // create new web view and engine
        WebView newWebView = new WebView();
        WebEngine newWebEngine = newWebView.getEngine();

        newWebEngine.load(originalWebEngine.getLocation());

        newTab.setContent(newWebView);

        return newTab;
    }
}
