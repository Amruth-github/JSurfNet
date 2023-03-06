package com.example.jsurfnet.utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tab;

public class TabSelection {
    private final ObjectProperty<Tab> selectedTabProperty = new SimpleObjectProperty<>();

    private static Tab tab;

    public TabSelection(Tab tab) {
        TabSelection.tab = tab;
    }

    public static Tab getSelectedTab() {
        return tab;
    }
}


