package com.example.jsurfnet.services;

import com.example.jsurfnet.Prototype.TabImpl;
import com.example.jsurfnet.Prototype.TabPrototype;
import com.example.jsurfnet.singleton.TabsAndWv;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;

public class TabBuilder {
    private Tab tab = null;

    public TabBuilder(Tab newtab) {
        this.tab = newtab;
        ContextMenu contextMenu = new ContextMenu();
        MenuItem duplicateItem = new MenuItem("Duplicate");

        // Prototype Pattern
        duplicateItem.setOnAction(actionEvent -> {
            Tab selectedTab = TabsAndWv.getInstance().getTabPane().getSelectionModel().getSelectedItem();
            TabPrototype prototype = new TabImpl(selectedTab);
            Tab newTab = prototype.clone();
            TabsAndWv.getInstance().getTabPane().getTabs().add(newTab);
        });
        contextMenu.getItems().add(duplicateItem);
        tab.setContextMenu(contextMenu);
    }
    public Tab getTab() {
        return tab;
    }
}
