package com.example.jsurfnet.utils;

import java.io.Serial;
import java.io.Serializable;

public final class CurrentUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static CurrentUser instance;
    private String username;
    private String password;

    private CurrentUser() {
        // Private constructor to prevent instantiation from outside the class
    }

    public static synchronized CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public static synchronized void setInstance(CurrentUser currentUser) {
        instance = currentUser;
    }


    public void setUsername(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
