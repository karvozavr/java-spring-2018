package ru.spbau.mit.karvozavr.tictactoe.core;

/**
 * Tic-Tac-Toe game controller
 */
public class GameController {

    private final GameField field;
    private final GameAgent playerX;
    private final GameAgent playerO;
    private CellType currentTurn = CellType.X;
    private boolean isGameEnded = false;
    private GameResult gameResult;

    public GameController(GameAgent playerX, GameAgent playerO) {
        field = new GameField();
        this.playerX = playerX;
        this.playerO = playerO;
        startGame();
    }

    public GameField getField() {
        return field;
    }

    private void startGame() {
        do {
            if (currentTurn == CellType.X) {
                playerX.takeTurn(field);
                currentTurn = CellType.O;
            } else {
                playerO.takeTurn(field);
                currentTurn = CellType.X;
            }
            getGameResult();
        } while (!isGameEnded);
    }

    public GameResult getGameResult() {
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
