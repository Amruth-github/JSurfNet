package com.example.jsurfnet.controllers;
import com.example.jsurfnet.utils.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import org.bson.Document;


public class BookmarksController implements Initializable {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    private String oldbookmark_name = "";

    public VBox bookmarksBox;
    public VBox bookmarkContainer;
    @FXML
    private FlowPane bookmarkPane = new FlowPane();

    private final String username;

    private static ObservableSet<Bookmark> bookmarks = FXCollections.observableSet();

    public BookmarksController() {
        // Initialize the MongoDB connection
        database = MongoDriver.getMongo();
        collection = database.getCollection("bookmarks");
        CurrentUser currentUser = CurrentUser.getInstance();
        this.username = currentUser.getUsername();
    }

    public void addBookmark(String name, String url, boolean from_ui) throws IOException {
        if (search(url)) {
            Bookmark bookmark = new Bookmark(name, url);
            bookmarks.add(bookmark);

            Button bookmarkButton = new Button(name);
            bookmarkButton.setGraphic(new Icon(url).getImage());

            bookmarkButton.setOnAction(event -> {
                loadBookmark(url);
            });
            if (from_ui && !CurrentUser.getInstance().getUsername().equals("guest")) {
                Document document = new Document()
                        .append("user", username)
                        .append("name", bookmark.getName())
                        .append("url", bookmark.getUrl());
                collection.insertOne(document);
            }

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
    public void loadBookmark(String url)
    {
        Tab tab = TabSelection.getSelectedTab();
        ToolBarController tbc = new ToolBarController();
        try {
            tbc.loadURL(url, tab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void renameBookmark(Button newBookmarkButton,TextField textField,Popup popup)
    {
        oldbookmark_name = newBookmarkButton.getText();
        textField.setText(oldbookmark_name);
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
        if (!CurrentUser.getInstance().getUsername().equals("guest")) {
            collection.deleteOne(Filters.and(
                    Filters.eq("user", username),
                    Filters.eq("url", bookmark.getUrl())
            ));
        }
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
                        MenuItem m3 = new MenuItem("Copy URL");
                        c.getItems().addAll(m1, m2, m3);
                        TextField textField = new TextField();
                        Popup popup = new Popup();
                        popup.getContent().add(textField);
                        textField.setOnAction(actionEvent -> {
                            newBookmarkButton.setText(textField.getText());
                            if (!CurrentUser.getInstance().getUsername().equals("guest")) {
                                collection.updateOne(Filters.and(
                                        Filters.eq("user", username),
                                        Filters.eq("name", oldbookmark_name)
                                ), Updates.set("name", newBookmarkButton.getText()));
                            }
                            popup.hide();
                        });
                        m1.setOnAction(actionEvent -> {
                            renameBookmark(newBookmarkButton,textField,popup);
                        });
                        m2.setOnAction(actionEvent -> {
                            removeBookmark((newBookmark));
                        });
                        m3.setOnAction(actionEvent -> {
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            ClipboardContent content = new ClipboardContent();
                            content.putString(newBookmark.getUrl());
                            clipboard.setContent(content);
                        });
                        newBookmarkButton.setContextMenu(c);
                        newBookmarkButton.setGraphic(new Icon(newBookmark.getUrl()).getImage());
                        newBookmarkButton.setOnAction(event -> {
                            loadBookmark(newBookmark.getUrl());
                        });
                        FlowPane.setMargin(newBookmarkButton, new Insets(0, 10, 0, 0)); // right padding of 10 pixels

                        bookmarkPane.getChildren().add(newBookmarkButton);

                    }
            });

            FindIterable<Document> iterable = collection.find(Filters.eq("user", username));
            for (Document document : iterable) {
                String name = document.getString("name");
                String bookmarksurl = document.getString("url");
                addBookmark(name, bookmarksurl, false);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
