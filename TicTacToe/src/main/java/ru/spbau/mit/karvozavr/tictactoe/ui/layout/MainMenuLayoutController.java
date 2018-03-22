package ru.spbau.mit.karvozavr.tictactoe.ui.layout;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import ru.spbau.mit.karvozavr.tictactoe.GameStatisticsManager;
import ru.spbau.mit.karvozavr.tictactoe.core.agent.GameAgentFactory;
import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.core.util.GameSetup;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainMenuLayoutController implements Initializable {

    @FXML
    private BorderPane mainView;
    @FXML
    private MenuItem restartGameButton;

    private GameLayoutController gameLayoutController;
    private GameSetup currentGameSetup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void startNewGame(ActionEvent actionEvent) {
        GameSetupController setupController = (GameSetupController) setMainView("/GameSetupLayout.fxml");
        setupController.injectMainController(this);
    }

    public void onGameSetupReady(String playerX, String playerO) {
        restartGameButton.setDisable(false);
        gameLayoutController = (GameLayoutController) setMainView("/GameLayout.fxml");
        currentGameSetup = new GameSetup(
            GameAgentFactory.agent(playerX, CellType.X, gameLayoutController),
            GameAgentFactory.agent(playerO, CellType.O, gameLayoutController)
        );

        gameLayoutController.newGame(currentGameSetup);
    }

    public void restartGame(ActionEvent actionEvent) {
        gameLayoutController.newGame(currentGameSetup);
    }

    public void exitGame(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit game.");
        alert.setHeaderText("Are you sure you what to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
            Platform.exit();
    }

    public void showStats(ActionEvent actionEvent) {
        restartGameButton.setDisable(true);
        GameStatsLayoutController gameStatsController = (GameStatsLayoutController) setMainView("/GameStatisticsLayout.fxml");
        gameStatsController.setGameStatsView();
    }

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
