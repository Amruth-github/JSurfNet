package com.example.jsurfnet.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class BrowserController {

    private Pane view;

    public Pane getPage(String filename){
        try{
            URL fileUrl = Objects.requireNonNull(getClass().getResource("/fxml/"+filename+".fxml"));
            if(fileUrl == null){
                throw new java.io.FileNotFoundException("FXML Not Found");
            }
            view = FXMLLoader.load(fileUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }
}
