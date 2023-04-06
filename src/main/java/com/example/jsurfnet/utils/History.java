package com.example.jsurfnet.utils;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class History implements java.io.Serializable {
    private String url;
    private String time;

    public History(String url) {
        this.url = url;
        this.time = new Date().toString();
    }

    public String toString() {
        return "[" + url + " " + time + "]";
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}