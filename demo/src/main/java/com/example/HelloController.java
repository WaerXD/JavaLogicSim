package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HelloController {
    @FXML
    protected void onStartButton(ActionEvent actionEvent) {
        HelloApplication.loadAndSet("board.fxml");
        HelloApplication.primaryStage.setResizable(true);
        HelloApplication.primaryStage.setHeight(700);
        HelloApplication.primaryStage.setWidth(1200);
    }
}