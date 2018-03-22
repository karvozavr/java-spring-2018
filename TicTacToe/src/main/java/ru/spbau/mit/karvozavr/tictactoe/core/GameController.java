package ru.spbau.mit.karvozavr.tictactoe.core;

import ru.spbau.mit.karvozavr.tictactoe.core.agent.GameAgent;
import ru.spbau.mit.karvozavr.tictactoe.core.util.GameResult;
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
    private Thread gameThread;

    /**
     * Creates GameController, starts new game in new thread.
     *
     * @param layoutController game layout controller
     * @param playerX          player X agent
     * @param playerO          player O agent
     */
    public GameController(GameLayoutController layoutController, GameAgent playerX, GameAgent playerO) {
        field = new GameField();
        this.layoutController = layoutController;
        this.playerX = playerX;
        this.playerO = playerO;

        // Start game logic in new Thread
        gameThread = new Thread(this::gameCycle);
        gameThread.start();
    }

    public GameAgent getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Ends game, interrupting game logic thread.
     */
    public void interruptGame() {
        gameThread.interrupt();
    }

    /**
     * Tic-Tac-Toe game cycle.
     */
    private void gameCycle() {
        currentPlayer = playerX;
        layoutController.drawField(field);
        do {
            if (Thread.currentThread().isInterrupted())
                return;

            if (currentPlayer == playerX) {
                playerX.takeTurn(field);
                currentPlayer = playerO;
            } else {
                playerO.takeTurn(field);
                currentPlayer = playerX;
            }
            layoutController.drawField(field);
            getGameResult();
        } while (!isGameEnded);
        layoutController.onGameFinished(getGameResult());
    }

    /**
     * Calculates current game state.
     *
     * @return game result
     */
    private GameResult getGameResult() {
        if (isGameEnded)
            return gameResult;
        gameResult = field.checkForGameEnd();
        if (gameResult != GameResult.NOT_FINISHED)
            isGameEnded = true;
        return gameResult;
    }
}
