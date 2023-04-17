package com.example.jsurfnet.services;

import com.example.jsurfnet.controllers.PasswordTable;
import com.example.jsurfnet.views.webHistoryView;

// Factory Pattern with concrete implementations in webHistoryView and PasswordTable
abstract public class TableFactory {

    abstract public void render();

    static public TableFactory getTable(String s) throws Exception {
        if (s.equals("PasswordTable")){
            return new PasswordTable();
        }
        if (s.equals("HistoryTable")){
            return new webHistoryView();
        }
        return null;
    }

}