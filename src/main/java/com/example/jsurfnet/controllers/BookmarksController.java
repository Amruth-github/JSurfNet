package com.example.jsurfnet.controllers;

import com.example.jsurfnet.utils.Bookmark;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.example.jsurfnet.utils.TabSelection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;


public class BookmarksController implements Initializable {

    public VBox bookmarksBox;
    public VBox bookmarkContainer;
    @FXML
    private FlowPane bookmarkPane = new FlowPane();

    private static ObservableSet<Bookmark> bookmarks = FXCollections.observableSet();

    public void addBookmark(String name, String url) throws IOException {
        if (search(url)) {
            Bookmark bookmark = new Bookmark(name, url);
            bookmarks.add(bookmark);

            Button bookmarkButton = new Button(name);
            bookmarkButton.setGraphic(new Icon(url).getImage());

            bookmarkButton.setOnAction(event -> {
                Tab tab = TabSelection.getSelectedTab();
                TabsController tc = new TabsController();
                ToolBarController tbc = new ToolBarController();
                try {
                    tbc.loadURL(url, tab);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private boolean search(String url) {
        for (Bookmark i : bookmarks) {
            if (i.getUrl().equals(url)) {
                return false;
            }
        }
        return true;
    }
    public void renameBookmark(Button newBookmarkButton,TextField textField,Popup popup)
    {
        textField.setText(newBookmarkButton.getText());
        double x = newBookmarkButton.localToScreen(newBookmarkButton.getLayoutBounds().getMaxX(), 0).getX();
        double y = newBookmarkButton.localToScreen(0, newBookmarkButton.getLayoutBounds().getMaxY()).getY();
        popup.show(newBookmarkButton, x, y);
        textField.requestFocus();
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
        try {
            bookmarks.addListener((SetChangeListener<Bookmark>) change -> {
                Bookmark newBookmark = change.getElementAdded();
                    if (change.wasAdded()) {
                        Button newBookmarkButton = new Button(newBookmark.getName());
                        ContextMenu c = new ContextMenu();
                        MenuItem m1 = new MenuItem("Rename Bookmark");
                        MenuItem m2 = new MenuItem("Remove Bookmark");
                        c.getItems().addAll(m1, m2);
                        TextField textField = new TextField();
                        Popup popup = new Popup();
                        popup.getContent().add(textField);
                        textField.setOnAction(actionEvent -> {
                            newBookmarkButton.setText(textField.getText());
                            popup.hide();
                        });
                        m1.setOnAction(actionEvent -> {
                            renameBookmark(newBookmarkButton,textField,popup);
                        });
                        m2.setOnAction(actionEvent -> {
                            removeBookmark((newBookmark));
                        });
                        newBookmarkButton.setContextMenu(c);
                        newBookmarkButton.setGraphic(new Icon(newBookmark.getUrl()).getImage());
                        newBookmarkButton.setOnAction(event -> {
                            Tab tab = TabSelection.getSelectedTab();
                            ToolBarController tbc = new ToolBarController();
                            try {
                                tbc.loadURL(newBookmark.getUrl(), tab);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        bookmarkPane.getChildren().add(newBookmarkButton);

                    }
            });

            addBookmark("Google", "https://www.google.com/");
            addBookmark("Github", "https://www.github.com/");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
