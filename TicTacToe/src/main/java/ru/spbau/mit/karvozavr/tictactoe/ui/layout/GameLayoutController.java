package ru.spbau.mit.karvozavr.tictactoe.ui.layout;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.core.GameController;
import ru.spbau.mit.karvozavr.tictactoe.core.GameField;
import ru.spbau.mit.karvozavr.tictactoe.core.util.GameResult;
import ru.spbau.mit.karvozavr.tictactoe.core.agent.GameAgent;
import ru.spbau.mit.karvozavr.tictactoe.core.util.GameSetup;

import java.net.URL;
import java.util.ResourceBundle;

public class GameLayoutController implements Initializable {

    @FXML
    private GridPane fieldGrid;
    @FXML
    private Label currentTurnLabel;
    /*@FXML
    public VBox menuLayout;
    @FXML
    private MainMenuLayoutController menuLayoutController;*/

    private GameController gameController;

    private Pair<Integer, Integer> lastTurn;

    private GameSetup currentGameSetup;

    @FXML
    public void onFieldButtonPressed(ActionEvent actionEvent) {
        onTurnStart();
        Button button = (Button) actionEvent.getSource();
        synchronized (this) {
            int lastTurnId = Integer.parseInt((String) button.getUserData());
            lastTurn = new Pair<>(lastTurnId / 3, lastTurnId % 3);
            notify();
        }
        onTurnEnd();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //menuLayoutController.injectGameLayoutController(this);
    }

    public void newGame(GameSetup gameSetup) {
        this.currentGameSetup = gameSetup;

        if (gameController != null)
            gameController.interruptGame();

        gameController = new GameController(this, currentGameSetup.getGameAgentX(), currentGameSetup.getGameAgentO());
        onGameStart();
    }

    private void onGameStart() {
        Platform.runLater(() -> {
            setTurnMessage(String.format("It is %s turn.", gameController.getCurrentPlayer()));
        });
    }

    public void onTurnStart() {
        Platform.runLater(() -> {
            setTurnMessage(String.format("It is %s turn.", gameController.getCurrentPlayer()));
            fieldGrid.setDisable(true);
        });
    }

    public void onTurnEnd() {
        Platform.runLater(() -> {
            fieldGrid.setDisable(false);
        });
    }

    private void setTurnMessage(String message) {
        Platform.runLater(() ->
            currentTurnLabel.setText(message));
    }

    public void onFieldUpdate(GameField field) {
        Platform.runLater(() -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    CellType cellType = field.getCell(i, j);
                    updateCell((Button) fieldGrid.getChildren().get(i * 3 + j), cellType);
                }
            }
        });
    }

    private void updateCell(Button cell, CellType cellType) {
        switch (cellType) {
            case O:
                cell.setText("O");
                cell.setDisable(true);
                break;
            case X:
                cell.setText("X");
                cell.setDisable(true);
                break;
            default:
                cell.setText("");
                cell.setDisable(false);
                break;
        }
    }

    public void onGameFinished(GameResult gameResult) {
        // TODO
        Platform.runLater(() -> {
            currentGameSetup.setGameResult(gameResult);
            fieldGrid.getChildren().forEach(cell -> cell.setDisable(true));
            setTurnMessage(String.format("Game finished: %s", gameResult.toString()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(gameResult.toString());
            alert.showAndWait();
        });
    }

    public synchronized Pair<Integer, Integer> getNextTurn() {
        while (lastTurn == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        Pair<Integer, Integer> result = lastTurn;
        lastTurn = null;
        return result;
    }
}

