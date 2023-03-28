package com.example.jsurfnet.utils;

public final class CurrentUser {

    private static CurrentUser instance;
    private String username;

    private CurrentUser() {
        // Private constructor to prevent instantiation from outside the class
    }

    public static synchronized CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
