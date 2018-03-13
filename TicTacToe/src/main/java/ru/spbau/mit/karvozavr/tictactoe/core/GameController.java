package ru.spbau.mit.karvozavr.tictactoe.core;

import ru.spbau.mit.karvozavr.tictactoe.ui.layout.GameLayoutController;

/**
 * Tic-Tac-Toe game controller
 */
public class GameController {

    private final GameField field;
    private final GameAgent playerX;
    private final GameAgent playerO;
    private final GameLayoutController layoutController;
    private GameAgent currentPlayer;
    private boolean isGameEnded = false;
    private GameResult gameResult;

    public GameController(GameLayoutController layoutController, GameAgent playerX, GameAgent playerO) {
        field = new GameField();
        this.layoutController = layoutController;
        this.playerX = playerX;
        this.playerO = playerO;
        // FIXME thread kill
        Thread gameThread = new Thread(this::startGame);
        gameThread.start();
    }

    public GameField getField() {
        return field;
    }

    public GameAgent getCurrentPlayer() {
        return currentPlayer;
    }

    public void startGame() {
        currentPlayer = playerX;
        do {
            if (currentPlayer == playerX) {
                playerX.takeTurn(field);
                currentPlayer = playerO;
            } else {
                playerO.takeTurn(field);
                currentPlayer = playerX;
            }
            layoutController.onFieldUpdate(field);
            getGameResult();
        } while (!isGameEnded);
        layoutController.onGameFinished(getGameResult());
    }

    private GameResult getGameResult() {
        if (isGameEnded)
            return gameResult;
        gameResult = field.checkForGameEnd();
        if (gameResult != null)
            isGameEnded = true;
        return gameResult;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }
}
