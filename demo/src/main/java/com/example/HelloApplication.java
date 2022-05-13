package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public static Stage primaryStage;
    static HelloApplication helloApplication;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        helloApplication = this;
        primaryStage.setTitle("Hello!");
        loadAndSet("startMenu.fxml");
    }

    public static FXMLLoader loadAndSet(String url) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(helloApplication.getClass().getResource(url));

        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert parent != null;
        Scene scene = new Scene(parent, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);

        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }
}