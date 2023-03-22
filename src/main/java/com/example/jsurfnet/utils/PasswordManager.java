package com.example.jsurfnet.utils;

import com.example.jsurfnet.controllers.TabsController;

import java.util.HashMap;

class Password {

    Password(String username, String password) {
        this.username = username;
        this.password = password;
    }
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
}

public class PasswordManager {
    private HashMap<String, Password> url_to_passwords = null;

    public void addCreds(String url, String username, String password) {
        if (url_to_passwords == null) {
            url_to_passwords = new HashMap<>();
        }
        url_to_passwords.put(TabsController.gethost(url), new Password(username, password));
    }

    public boolean exists(String url) {
        Password pwd = url_to_passwords.get(TabsController.gethost(url));
        return (pwd != null);
    }

    public Password getCreds(String url) {
        return url_to_passwords.get(TabsController.gethost(url));
    }
}
