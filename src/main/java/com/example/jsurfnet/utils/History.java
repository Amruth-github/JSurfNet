package com.example.jsurfnet.utils;

import java.sql.Time;

public class History implements java.io.Serializable {
    private String url;
    private Time time;

    public History(String url, Time time) {
        this.url = url;
        this.time = time;
    }

    public String toString() {
        return "[" + url + " " + time.toString() + "]";
    }

    public Time getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}