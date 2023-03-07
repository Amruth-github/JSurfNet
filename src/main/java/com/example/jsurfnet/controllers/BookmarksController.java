package com.example.jsurfnet.controllers;

import com.example.jsurfnet.utils.Bookmark;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.jsurfnet.utils.TabSelection;

public class BookmarksController implements Initializable {

    @FXML
    private FlowPane bookmarkPane;

    private List<Bookmark> bookmarks = new ArrayList<>();



    public void addBookmark(String name, String url) {
        Bookmark bookmark = new Bookmark(name, url);
        bookmarks.add(bookmark);

        Button bookmarkButton = new Button(name);

        bookmarkButton.setOnAction(event -> {
            Tab tab = TabSelection.getSelectedTab();
            if (tab==null){
                System.out.println("Hmm here");
            }
            TabsController tc = new TabsController();
            tc.loadURL(url, tab);
        });

        bookmarkPane.getChildren().add(bookmarkButton);
    }

    public void removeBookmark(Bookmark bookmark) {
        bookmarks.remove(bookmark);
        bookmarkPane.getChildren().removeIf(node -> {
            Button button = (Button) node;
            return button.getText().equals(bookmark.getName());
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBookmark("Google", "https://www.google.com/");
        addBookmark("Github", "https://www.github.com/");

    }
}
