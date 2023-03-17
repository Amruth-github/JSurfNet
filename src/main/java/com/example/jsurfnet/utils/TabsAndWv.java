package com.example.jsurfnet.utils;

import javafx.scene.control.TabPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public final class TabsAndWv {

    static private TabPane tabPane;
    static private WebView webView;
    static private WebEngine engine;

    private TabsAndWv(){
        TabsAndWv tabsAndWv = new TabsAndWv();
    }

    public static TabPane getTabPane(){
        return tabPane;
    }

    public static WebView getWebView(){
        return webView;
    }

    public static WebEngine getWebEngine(){
        return engine;
    }

    public static void setWebEngine(WebEngine we){
        engine = we;
    }

    public static void setWebView(WebView wv){
        webView = wv;
    }
    public static void setTabPane(TabPane tp){
        tabPane = tp;
    }



}
