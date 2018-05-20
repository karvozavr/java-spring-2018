package ru.spbau.mit.karvozavr.tictactoe.ui.layout;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import ru.spbau.mit.karvozavr.tictactoe.core.GameController;
import ru.spbau.mit.karvozavr.tictactoe.core.GameField;
import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.core.util.GameResult;
import ru.spbau.mit.karvozavr.tictactoe.core.util.GameSetup;

/**
 * Tic-Tac-Toe game  UI controller.
 */
public class GameLayoutController {

    @FXML
    private GridPane fieldGrid;
    @FXML
    private Label currentTurnLabel;

    private GameController gameController;
    private Integer lastTurn;
    private GameSetup currentGameSetup;

    /**
     * Handles game field cell pressed.
     *
     * @param actionEvent event
     */
    @FXML
    private synchronized void onFieldButtonPressed(ActionEvent actionEvent) {
        onTurnStart();
        Button button = (Button) actionEvent.getSource();
        synchronized (this) {
            lastTurn = Integer.parseInt((String) button.getUserData());
            notify();
        }
        onTurnEnd();
    }

    /**
     * Start new game with given setup.
     *
     * @param gameSetup new game setup
     */
    public void newGame(GameSetup gameSetup) {
        this.currentGameSetup = gameSetup;

        if (gameController != null)
            gameController.interruptGame();

        gameController = new GameController(this, currentGameSetup.getGameAgentX(), currentGameSetup.getGameAgentO());
        onGameStart();
    }

    /**
     * Turn start callback.
     */
    public void onTurnStart() {
        Platform.runLater(() -> {
            setTurnMessage(String.format("It is %s turn.", gameController.getCurrentPlayer().getTypeName()));
            fieldGrid.setDisable(true);
        });
    }

    /**
     * Turn end callback.
     */
    public void onTurnEnd() {
        Platform.runLater(() -> {
            fieldGrid.setDisable(false);
        });
    }

    /**
     * Draws game field.
     *
     * @param field game field to draw
     */
    public void drawField(GameField field) {
        Platform.runLater(() -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    CellType cellType = field.getCell(i, j);
                    drawCell((Button) fieldGrid.getChildren().get(i * 3 + j), cellType);
                }
            }
        });
    }

    /**
     * Game finish callback.
     *
     * @param gameResult game result
     */
    public void onGameFinished(GameResult gameResult) {
        Platform.runLater(() -> {
            fieldGrid.getChildren().forEach(cell -> cell.setDisable(true));
            setTurnMessage(String.format("Game finished: %s", gameResult.toString()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(gameResult.toString());
            alert.showAndWait();
        });
        currentGameSetup.setGameResult(gameResult);
        currentGameSetup.saveGameStatistics();
    }

    /**
     * Waits for the next player turn.
     *
     * @return turn
     */
    public synchronized Integer getNextTurn() {
        while (lastTurn == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        Integer result = lastTurn;
        lastTurn = null;
        return result;
    }

    /**
     * Game start callback.
     */
    private void onGameStart() {
        Platform.runLater(() -> {
            setTurnMessage(String.format("It is %s turn.", gameController.getCurrentPlayer().getTypeName()));
        });
    }

    /**
     * Draws cell.
     *
     * @param cell     cell to draw
     * @param cellType value
     */
    private void drawCell(Button cell, CellType cellType) {
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

    /**
     * Sets turn message in UI thread.
     *
     * @param message new message
     */
    private void setTurnMessage(String message) {
        Platform.runLater(() ->
            currentTurnLabel.setText(message));
    }
}
