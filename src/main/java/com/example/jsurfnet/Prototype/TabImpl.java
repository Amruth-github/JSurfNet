package com.example.jsurfnet.Prototype;

import com.example.jsurfnet.controllers.TabsController;
import com.example.jsurfnet.services.IconBuilder;
import com.example.jsurfnet.singleton.TabsAndWv;
import javafx.concurrent.Worker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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

        ContextMenu cm = new ContextMenu();
        MenuItem duplicateItem = new MenuItem("Duplicate");
        cm.getItems().add(duplicateItem);
        duplicateItem.setOnAction(actionEvent -> {
            Tab selectedTab = TabsAndWv.getInstance().getTabPane().getSelectionModel().getSelectedItem();
            TabPrototype prototype = new TabImpl(selectedTab);
            Tab nT = prototype.clone();
            TabsAndWv.getInstance().getTabPane().getTabs().add(nT);
        });
        newTab.setContextMenu(cm);

        new TabsController().ListnersForWebView(newTab, newWebEngine, TabsAndWv.getInstance().getTabPane());

        return newTab;
    }
}
