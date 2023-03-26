package com.example.jsurfnet.controllers;
import com.example.jsurfnet.utils.CurrentUser;
import com.example.jsurfnet.utils.Icon;
import com.example.jsurfnet.utils.Bookmark;
import com.mongodb.client.model.Filters;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.jsurfnet.utils.TabSelection;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import org.bson.Document;


public class BookmarksController implements Initializable {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public VBox bookmarksBox;
    public VBox bookmarkContainer;
    @FXML
    private FlowPane bookmarkPane = new FlowPane();

    private final String username;

    private static ObservableSet<Bookmark> bookmarks = FXCollections.observableSet();

    public BookmarksController() {
        // Initialize the MongoDB connection
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase("JSurNet");
        collection = database.getCollection("bookmarks");
        CurrentUser currentUser = CurrentUser.getInstance();
        this.username = currentUser.getUsername();
    }

    public void addBookmark(String name, String url) throws IOException {
        if (search(url)) {
            Bookmark bookmark = new Bookmark(name, url);
            bookmarks.add(bookmark);

            Button bookmarkButton = new Button(name);
            bookmarkButton.setGraphic(new Icon(url).getImage());

            bookmarkButton.setOnAction(event -> {
                loadBookmark(url);
            });

            Document document = new Document()
                    .append("user", username)
                    .append("name", bookmark.getName())
                    .append("url", bookmark.getUrl());
            collection.insertOne(document);

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
        collection.deleteOne(Filters.and(
                Filters.eq("user", username),
                Filters.eq("url", bookmark.getUrl())
        ));
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
                            loadBookmark(newBookmark.getUrl());
                        });
                        bookmarkPane.getChildren().add(newBookmarkButton);

                    }
            });

            FindIterable<Document> iterable = collection.find(Filters.eq("user", username));
            for (Document document : iterable) {
                String name = document.getString("name");
                String bookmarksurl = document.getString("url");
                addBookmark(name, bookmarksurl);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
