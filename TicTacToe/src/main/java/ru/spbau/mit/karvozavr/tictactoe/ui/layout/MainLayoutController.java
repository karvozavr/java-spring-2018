package ru.spbau.mit.karvozavr.tictactoe.ui.layout;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import ru.spbau.mit.karvozavr.tictactoe.core.agent.GameAgentFactory;
import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.core.util.GameSetup;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main app layout controller.
 */
public class MainLayoutController {

    @FXML
    private BorderPane mainView;
    @FXML
    private MenuItem restartGameButton;

    private GameLayoutController gameLayoutController;
    private GameSetup currentGameSetup;

    /**
     * Starts new game with given setup.
     *
     * @param actionEvent event
     */
    public void startNewGame(ActionEvent actionEvent) {
        GameSetupLayoutController setupController = (GameSetupLayoutController) setMainView("/GameSetupLayout.fxml");
        setupController.injectMainController(this);
    }

    /**
     * Game setup callback.
     *
     * @param playerX player X agent name
     * @param playerO player Y agent name
     */
    public void onGameSetupReady(String playerX, String playerO) {
        restartGameButton.setDisable(false);
        gameLayoutController = (GameLayoutController) setMainView("/GameLayout.fxml");
        currentGameSetup = new GameSetup(
            GameAgentFactory.agent(playerX, CellType.X, gameLayoutController),
            GameAgentFactory.agent(playerO, CellType.O, gameLayoutController)
        );

        gameLayoutController.newGame(currentGameSetup);
    }

    /**
     * Restarts the game with the same setup
     *
     * @param actionEvent event
     */
    public void restartGame(ActionEvent actionEvent) {
        gameLayoutController.newGame(currentGameSetup);
    }

    /**
     * Terminates application.
     *
     * @param actionEvent event
     */
    public void exitGame(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit game.");
        alert.setHeaderText("Are you sure you what to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
            Platform.exit();
    }

    /**
     * Shows statistics.
     *
     * @param actionEvent event
     */
    public void showStats(ActionEvent actionEvent) {
        restartGameButton.setDisable(true);
        GameStatsLayoutController gameStatsController = (GameStatsLayoutController) setMainView("/GameStatisticsLayout.fxml");
        gameStatsController.setGameStatsView();
    }

    /**
     * Changes current scene.
     *
     * @param name Node resource name
     * @return new scene controller
     */
    private Object setMainView(String name) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(name));
            Node gameSetupLayout = fxmlLoader.load();
            mainView.setCenter(gameSetupLayout);
            return fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Internal error: failed to load FXML resources.", e);
        }
    }
}
