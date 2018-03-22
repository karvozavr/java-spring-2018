package ru.spbau.mit.karvozavr.tictactoe.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.spbau.mit.karvozavr.tictactoe.GameStatisticsManager;

/**
 * Tic-Tac-Toe game app Main class.
 */
public class TicTacToeApp extends Application {

    @Override
    public void stop() throws Exception {
        super.stop();
        // Flush statistics on exit.
        GameStatisticsManager.onAppStop();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent menuRoot = FXMLLoader.load(getClass().getResource("/MainLayout.fxml"));
        Scene mainMenuScene = new Scene(menuRoot, 600, 800);
        stage.setTitle("Tic Tac Toe");
        stage.setMinHeight(800);
        stage.setMinWidth(600);
        stage.setScene(mainMenuScene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(TicTacToeApp.class, args);
    }
}
