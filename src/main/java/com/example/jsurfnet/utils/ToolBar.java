package com.example.jsurfnet.utils;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public final class ToolBar {
    static private Button backButton;
    static private Button forwardButton;
    static private Button reloadButton;
    static private TextField urlField;
    static private Button searchButton;
    static private Button newTabButton;
    static private Button newBookmarkButton;

    static private Button logoutButton;

    private static ToolBar instance = null;

    static private Button showPassword;
    private static Button homeButton;
    private webHistory history;
    private Button showHistory;

    private ToolBar(){
    }

    public static synchronized ToolBar getInstance() {
        if (instance == null) {
            instance = new ToolBar();
        }
        return instance;
    }

    public Button getBackButton(){
        return backButton;
    }
    public Button getForwardButton(){
        return forwardButton;
    }
    public Button getReloadButton(){
        return reloadButton;
    }
    public TextField getUrlField(){
        return urlField;
    }
    public Button getSearchButton(){
        return searchButton;
    }
    public Button getNewTabButton(){
        return newTabButton;
    }
    public Button getNewBookmarkButton(){
        return newBookmarkButton;
    }
    public Button getLogoutButton(){
        return logoutButton;
    }

    public void setBackButton(Button bb){
        backButton = bb;
    }
    public void setForwardButton(Button fb){
        forwardButton = fb;
    }
    public void setReloadButton(Button rb){
        reloadButton = rb;
    }
    public void setUrlField(TextField url){
        urlField = url;
    }
    public void setSearchButton(Button sb){
        searchButton = sb;
    }
    public void setNewTabButton(Button ntb){
        newTabButton = ntb;
    }
    public void setNewBookmarkButton(Button nbb){
        newBookmarkButton = nbb;
    }
    public void setLogoutButton(Button lb) {
        logoutButton = lb;
    }

    public Button getShowPassword() {
        return showPassword;
    }

    public void setShowPassword(Button showPassword) {
        this.showPassword = showPassword;
    }

    public void setHomeButton(Button homeButton) {
        this.homeButton = homeButton;
    }

    public void setHistory(webHistory history) {
        this.history = history;
    }

    public webHistory getHistory() {
        return history;
    }

    public void setHistoryButton(Button showHistory) {
        this.showHistory = showHistory;
    }

    public Button getShowHistory() {
        return showHistory;
    }
}
