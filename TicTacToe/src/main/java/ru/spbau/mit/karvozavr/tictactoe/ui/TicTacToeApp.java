package ru.spbau.mit.karvozavr.tictactoe.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.spbau.mit.karvozavr.tictactoe.GameStatisticsManager;

public class TicTacToeApp extends Application {

    private Scene mainMenuScene;
    private Scene gameScene;

    private Stage primaryStage;

    @Override
    public void stop() throws Exception {
        super.stop();
        GameStatisticsManager.onAppStop();
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Parent gameRoot = FXMLLoader.load(getClass().getResource("/GameLayout.fxml"));
        Parent menuRoot = FXMLLoader.load(getClass().getResource("/MainMenuLayout.fxml"));

        gameScene = new Scene(gameRoot, 600, 800);
        mainMenuScene = new Scene(menuRoot, 600, 800);

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
