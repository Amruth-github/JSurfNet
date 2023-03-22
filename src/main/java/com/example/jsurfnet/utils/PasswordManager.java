package com.example.jsurfnet.utils;

import com.example.jsurfnet.controllers.TabsController;

import java.util.HashMap;

public class PasswordManager {
    private HashMap<String, Credential> url_to_passwords = null;

    public void addCreds(String url, String username, String password) {
        if (url_to_passwords == null) {
            url_to_passwords = new HashMap<>();
        }
        url_to_passwords.put(TabsController.gethost(url), new Credential(username, password));
    }

    public boolean exists(String url) {
        try{
            Credential pwd = url_to_passwords.get(TabsController.gethost(url));
            System.out.println("Password exists");
            return true;
        } catch (Exception e) {
            System.out.println("Password does not exist");
            return false;
        }
    }

    public Credential getCreds(String url) {
        return url_to_passwords.get(TabsController.gethost(url));
    }
}
