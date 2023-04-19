package com.example.jsurfnet.views;

import com.example.jsurfnet.models.Credential;
import com.example.jsurfnet.services.Icon;
import com.example.jsurfnet.services.IconBuilder;
import com.example.jsurfnet.services.PasswordManager;
import com.example.jsurfnet.services.TableFactory;
import com.example.jsurfnet.singleton.TabsAndWv;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.HashMap;

public class PasswordTable extends TableFactory {

    private PasswordManager passwordManager = TabsAndWv.getInstance().getPasswordManager();

    private HashMap<String, Credential> urlToPassword = TabsAndWv.getInstance().getPasswordManager().getHash();
    private static Tab passwordTab;

    public PasswordTable() throws Exception {
    }

    @Override
    public void render() {
        if (passwordTab == null) {
            passwordTab = new Tab();
            passwordTab.setText("Passwords");
            passwordTab.setGraphic(new IconBuilder("/icons/padlock.png", 16, 16).getIcon());
        }

        TableView<Credential> tableView = new TableView<>();
        tableView.setPlaceholder(new Label("No Passwords!"));
        TableColumn<Credential, String> urlColumn = new TableColumn<>("URL");
        urlColumn.setCellValueFactory(data -> {
            String url = null;
            for (String key : urlToPassword.keySet()) {
                if (urlToPassword.get(key).equals(data.getValue())) {
                    url = key;
                    break;
                }
            }
            return new SimpleStringProperty(url);
        });
        urlColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item);
                    setGraphic(new Icon("https://" + item).getImage());
                }
            }
        });

        TableColumn<Credential, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Credential, String> passwordColumn = new TableColumn<>("Password");

        passwordColumn.setPrefWidth(100);
        passwordColumn.setCellFactory(column -> new TableCell<Credential, String>() {
            final Button showButton = new Button();
            boolean show = false;

            {
                showButton.setGraphic(new IconBuilder("/icons/show.png", 16, 16).getIcon());
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                TextField passwordField = new TextField();
                passwordField.setFont(new Font(10));
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox();
                    passwordField.setText("***");
                    hBox.setSpacing(10);
                    hBox.getChildren().addAll(passwordField, showButton);
                    setGraphic(hBox);
                }
                showButton.setOnAction(actionEvent -> {
                    if (!show) {
                        show = true;
                        passwordField.setText(passwordManager.getHash().get(urlColumn.getCellData(getIndex())).getPassword());
                    } else {
                        show = false;
                        passwordField.setText("***");
                    }
                });
                passwordField.setOnAction(e -> {
                    String url = urlColumn.getCellData(getIndex());
                    if (!passwordField.getText().equals("***")) {
                        passwordManager.updateCreds(url, passwordManager.getHash().get(url).getUsername(), passwordField.getText());
                    }
                    passwordField.setText("***");
                    try {
                        new PasswordTable().render();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                });
                setPrefWidth(100);
            }
        });


        VBox passwordList = new VBox();
        passwordList.setAlignment(Pos.TOP_LEFT);
        passwordList.setSpacing(10);
        passwordList.setPadding(new Insets(10));
        passwordList.setStyle("-fx-background-color: #1C1C1C;");

        passwordList.getChildren().add(tableView);

        ScrollPane scrollPane = new ScrollPane(passwordList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #1C1C1C;");

        tableView.getColumns().addAll(urlColumn, usernameColumn, passwordColumn);

        ObservableList<Credential> credentialList = FXCollections.observableArrayList();
        for (Credential credential : urlToPassword.values()) {
            credentialList.add(credential);
        }
        tableView.setItems(credentialList);


        tableView.setStyle("-fx-font-size: 14; -fx-font-family: 'Arial'; -fx-background-color: #1C1C1C; -fx-text-fill: white;");

        passwordTab.setContent(scrollPane);
        tableView.setStyle("-fx-font-size: 14; -fx-font-family: 'Arial'; -fx-background-color: #1C1C1C; -fx-text-fill: white;");

        if (!TabsAndWv.getInstance().getTabPane().getTabs().contains(passwordTab)) {
            TabsAndWv.getInstance().getTabPane().getTabs().add(passwordTab);
        }
        TabsAndWv.getInstance().getTabPane().getSelectionModel().select(passwordTab);
    }
}