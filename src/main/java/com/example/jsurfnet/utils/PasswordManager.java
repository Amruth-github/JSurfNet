package com.example.jsurfnet.utils;

import com.example.jsurfnet.controllers.TabsController;
import javafx.scene.web.WebEngine;

import java.util.HashMap;

public class PasswordManager {
    private HashMap<String, Credential> url_to_credential = null;

    public void addCreds(String url, String username, String password) {
        if (url_to_credential == null) {
            url_to_credential = new HashMap<>();
        }
        url_to_credential.put(TabsController.gethost(url), new Credential(username, password));
    }

    public boolean exists(String url) {
        Credential pwd = url_to_credential.get(TabsController.gethost(url));
        return pwd != null;
    }

    public void autofill(WebEngine webEngine, String url) {
        Credential required = url_to_credential.get(TabsController.gethost(url));

    }

    public Credential getCreds(String url) {
        return url_to_credential.get(TabsController.gethost(url));
    }
}
