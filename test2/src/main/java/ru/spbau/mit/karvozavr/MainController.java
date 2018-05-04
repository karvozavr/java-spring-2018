package ru.spbau.mit.karvozavr;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Pairs game layout controller.
 */
public class MainController {

    @FXML
    private GridPane gameFieldGrid;

    private volatile Button chosenCell = null;
    private int pairsLeft = 0;
    private boolean allowed = true;

    /**
     * Perform initialization.
     *
     * @param fieldSize  field size
     * @param windowSize window size
     */
    public void initializeLayout(int fieldSize, double windowSize) {
        pairsLeft = fieldSize * fieldSize / 2;
        ArrayList<Integer> numbers = generateNumbers(pairsLeft);
        gameFieldGrid.setAlignment(Pos.CENTER);

        for (int r = 0; r < fieldSize; r++) {
            for (int c = 0; c < fieldSize; c++) {
                int number = fieldSize * r + c;
                Button button = new Button();
                button.setUserData(numbers.get(number).toString());
                button.setMinWidth(windowSize / fieldSize);
                button.setMinHeight(windowSize / fieldSize);
                button.setPrefWidth(windowSize / fieldSize);
                button.setPrefHeight(windowSize / fieldSize);
                button.setOnAction(new GameCellClickHandler());
                gameFieldGrid.add(button, c, r);
            }
        }
    }

    /**
     * Generate random permutation of multiset of {0..n} U {0..n}.
     * @param n numbers
     * @return permutation
     */
    private ArrayList<Integer> generateNumbers(int n) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            numbers.add(i);
        }
        for (int i = 0; i < n; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers;
    }

    /**
     * Check for the game end.
     */
    private void checkForGameEnd() {
        if (pairsLeft == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You won!");
            alert.setHeaderText("You won!");
            alert.setContentText("Congratulations!");

            alert.showAndWait();
            System.exit(0);
        }
    }

    /**
     * Game cell click handler.
     */
    private class GameCellClickHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            synchronized (MainController.this) {
                if (!allowed)
                    return;

                Button button = (Button) actionEvent.getSource();
                if (!button.getText().equals(""))
                    return;

                button.setText((String) button.getUserData());
                if (chosenCell == null) {
                    chosenCell = button;
                } else {
                    if (chosenCell.getUserData().equals(button.getUserData())) {
                        // if correct pair
                        --pairsLeft;
                        chosenCell.setDisable(true);
                        button.setDisable(true);
                        chosenCell = null;
                        checkForGameEnd();
                    } else {
                        // if incorrect pair
                        allowed = false;
                        new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Platform.runLater(() -> {
                                button.setText("");
                                chosenCell.setText("");
                                chosenCell = null;
                                allowed = true;
                            });
                        }).start();
                    }
                }
            }
        }
    }
}
