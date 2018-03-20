package ru.spbau.mit.karvozavr.tictactoe.ui.layout;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.core.agent.PlayerAgent;
import ru.spbau.mit.karvozavr.tictactoe.core.agent.RandomBotAgent;
import ru.spbau.mit.karvozavr.tictactoe.core.util.GameSetup;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainMenuLayoutController implements Initializable {

    @FXML
    public MenuItem restartGameButton;

    private GameLayoutController gameLayoutController;
    private volatile GameSetup currentGameSetup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        restartGameButton.setDisable(true);
    }

    public void injectGameLayoutController(GameLayoutController gameLayoutController) {
        this.gameLayoutController = gameLayoutController;
    }

    public void handleKeyInput(KeyEvent keyEvent) {
    }

    public void startNewGame(ActionEvent actionEvent) {
        currentGameSetup = new GameSetup(new PlayerAgent(CellType.X, gameLayoutController), new RandomBotAgent(CellType.O, gameLayoutController));
        restartGameButton.setDisable(false);
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
            System.exit(0);
    }

    public void showStats(ActionEvent actionEvent) {
        // TODO
    }
}
