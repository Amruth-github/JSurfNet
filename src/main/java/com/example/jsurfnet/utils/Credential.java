package com.example.jsurfnet.utils;

public class Credential implements java.io.Serializable{

    Credential(String username, String password) {
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
