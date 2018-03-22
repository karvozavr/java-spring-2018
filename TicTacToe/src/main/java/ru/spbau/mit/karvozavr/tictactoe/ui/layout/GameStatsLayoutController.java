package ru.spbau.mit.karvozavr.tictactoe.ui.layout;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import ru.spbau.mit.karvozavr.tictactoe.GameStatisticsManager;

public class GameStatsLayoutController {

    @FXML
    private TableView<GameStatisticsManager.GameStats> gameStatsView;

    public void setGameStatsView() {
        for (GameStatisticsManager.GameStats item : GameStatisticsManager.getStatistics()) {
            gameStatsView.getItems().add(item);
        }
    }
}
