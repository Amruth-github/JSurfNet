package com.example.jsurfnet.services;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconBuilder {
    private String path;

    private ImageView iv;

    public IconBuilder(String path, int height, int width) {
        this.path = path;
        iv = new ImageView(new Image(getClass().getResourceAsStream(path)));
        iv.setFitHeight(height);
        iv.setFitWidth(width);
    }

    public ImageView getIcon() {
        return iv;
    }
}
