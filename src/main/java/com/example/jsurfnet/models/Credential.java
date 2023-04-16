package com.example.jsurfnet.models;

public class Credential implements java.io.Serializable{

    public Credential(String username, String password) {
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
