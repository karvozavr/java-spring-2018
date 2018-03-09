package ru.spbau.mit.karvozavr.tictactoe.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;

public class TicTacToeApp extends Application {

    private Scene mainMenuScene;
    private Scene gameScene;

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Parent gameRoot = FXMLLoader.load(new File("src/main/layout/GameLayout.fxml").toURI().toURL());
        Parent menuRoot = FXMLLoader.load(new File("src/main/layout/MainMenuLayout.fxml").toURI().toURL());

        gameScene = new Scene(gameRoot, 600, 800);
        mainMenuScene = new Scene(menuRoot, 600, 800);
        Button button = (Button) menuRoot.lookup("#start");
        button.setOnAction(actionEvent -> {
            primaryStage.setScene(gameScene);
        });

        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(TicTacToeApp.class, args);
    }
}
