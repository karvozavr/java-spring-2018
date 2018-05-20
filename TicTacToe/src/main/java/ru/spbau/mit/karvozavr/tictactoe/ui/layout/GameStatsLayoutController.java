package ru.spbau.mit.karvozavr.tictactoe.ui.layout;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import ru.spbau.mit.karvozavr.tictactoe.GameStatisticsManager;

/**
 * Game statistics layout controller.
 */
public class GameStatsLayoutController {

    @FXML
    private TableView<GameStatisticsManager.GameStats> gameStatsView;

    /**
     * Sets game statistics table content.
     */
    public void setGameStatsView() {
        for (GameStatisticsManager.GameStats item : GameStatisticsManager.getStatistics()) {
            gameStatsView.getItems().add(item);
        }
    }
}
