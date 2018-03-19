package ru.spbau.mit.karvozavr.tictactoe.ui.layout;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import ru.spbau.mit.karvozavr.tictactoe.core.CellType;
import ru.spbau.mit.karvozavr.tictactoe.core.GameController;
import ru.spbau.mit.karvozavr.tictactoe.core.GameField;
import ru.spbau.mit.karvozavr.tictactoe.core.GameResult;
import ru.spbau.mit.karvozavr.tictactoe.core.agent.GameAgent;

import java.net.URL;
import java.util.ResourceBundle;

public class GameLayoutController implements Initializable {

    @FXML
    private GridPane fieldGrid;
    @FXML
    private Label currentTurnMessage;

    private GameController gameController;

    private Pair<Integer, Integer> lastTurn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void newGame(GameAgent playerX, GameAgent playerO) {
        gameController = new GameController(this, playerX, playerO);
    }

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

    public void onTurnStart() {
        setTurnMessage();
        fieldGrid.setDisable(true);
    }

    public void onTurnEnd() {
        fieldGrid.setDisable(false);
    }

    private void setTurnMessage() {
        Platform.runLater(() ->
            currentTurnMessage.setText(String.format("It is %s turn.", gameController.getCurrentPlayer())));
    }

    public void onFieldUpdate(GameField field) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                CellType cellType = field.getCell(i, j);
                if (cellType != null)
                    updateCell((Button) fieldGrid.getChildren().get(i * 3 + j), cellType);
            }
        }
    }

    private void updateCell(Button cell, CellType cellType) {
        Platform.runLater(() -> {
            switch (cellType) {
                case O:
                    cell.setText("O");
                    break;
                case X:
                    cell.setText("X");
                    break;
            }

            cell.setDisable(true);
        });
    }

    public void onGameFinished(GameResult gameResult) {
        // TODO
        Platform.runLater(() -> {
            fieldGrid.setDisable(true);
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
                return null;
            }
        }

        Pair<Integer, Integer> result = lastTurn;
        lastTurn = null;
        return result;
    }
}

