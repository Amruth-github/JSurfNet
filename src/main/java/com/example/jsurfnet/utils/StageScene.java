package com.example.jsurfnet.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

public final class StageScene {

    static private Stage stage;

    static private Scene scene;
    private static StageScene instance = null;

    private StageScene(){}

    public static synchronized StageScene getInstance() {
        if (instance == null) {
            instance = new StageScene();
        }
        return instance;
    }

    public void setStage(Stage s){
        stage = s;
    }
    public void setScene(Scene s){
        scene = s;
    }

    public Stage getStage(){
        return stage;
    }
    public Scene getScene(){
        return scene;
    }
}
