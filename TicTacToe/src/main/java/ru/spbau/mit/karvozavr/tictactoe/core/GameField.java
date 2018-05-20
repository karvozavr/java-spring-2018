package ru.spbau.mit.karvozavr.tictactoe.core;

import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.core.util.GameResult;

/**
 * Tic-Tac-Toe game field.
 */
public class GameField {

    private static final int fieldSize = 3;
    private CellType[][] field = new CellType[fieldSize][fieldSize];

    /**
     * Construct empty game field.
     */
    public GameField() {
        for (int row = 0; row < fieldSize; row++) {
            for (int col = 0; col < fieldSize; col++) {
                field[row][col] = CellType.EMPTY;
            }
        }
    }

    /**
     * Set the state of a single field cell.
     *
     * @param row   row position of the cell
     * @param col   column position of the cell
     * @param state new state
     */
    public void setCell(int row, int col, CellType state) {
        if (field[row][col] == CellType.EMPTY) {
            field[row][col] = state;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Get the state of a single field cell.
     *
     * @param row row position of the cell
     * @param col column position of the cell
     * @return state of the cell
     */
    public CellType getCell(int row, int col) {
        return field[row][col];
    }

    public boolean isFull() {
        int filled = 0;

        for (int row = 0; row < fieldSize; row++) {
            for (int col = 0; col < fieldSize; col++) {
                filled += field[row][col] != CellType.EMPTY ? 1 : 0;
            }
        }

        return filled == fieldSize * fieldSize;
    }

    /**
     * Check if the game ended.
     *
     * @return result of a game (or GameResult.NOT_FINISHED if game is not finished)
     */
    public GameResult checkForGameEnd() {

        // Check rows
        for (int row = 0; row < fieldSize; row++) {
            boolean isWin = true;
            for (int col = 1; col < fieldSize; col++) {
                if (field[row][col] == CellType.EMPTY || field[row][col - 1] != field[row][col]) {
                    isWin = false;
                    break;
                }
            }
            if (isWin) {
                if (field[row][0] == CellType.X) {
                    return GameResult.X_WIN;
                } else {
                    return GameResult.O_WIN;
                }
            }
        }

        // Check columns
        for (int col = 0; col < fieldSize; col++) {
            boolean isWin = true;
            for (int row = 1; row < fieldSize; row++) {
                if (field[row][col] == CellType.EMPTY || field[row - 1][col] != field[row][col]) {
                    isWin = false;
                    break;
                }
            }
            if (isWin) {
                if (field[0][col] == CellType.X) {
                    return GameResult.X_WIN;
                } else {
                    return GameResult.O_WIN;
                }
            }
        }

        // Check diagonals
        boolean isUpperLeft = true;
        for (int i = 1; i < fieldSize; i++) {
            if (field[i][i] == CellType.EMPTY || field[i - 1][i - 1] != field[i][i]) {
                isUpperLeft = false;
                break;
            }
        }

        boolean isUpperRight = true;
        for (int i = 1; i < fieldSize; i++) {
            if (field[i][fieldSize - i - 1] == CellType.EMPTY || field[i - 1][fieldSize - i] != field[i][fieldSize - i - 1]) {
                isUpperRight = false;
                break;
            }
        }

        if (isUpperLeft || isUpperRight) {
            if (field[fieldSize / 2][fieldSize / 2] == CellType.X) {
                return GameResult.X_WIN;
            } else {
                return GameResult.O_WIN;
            }
        }

        // Check for draw
        if (isFull()) {
            return GameResult.DRAW;
        }

        // The game hasn't ended
        return GameResult.NOT_FINISHED;
    }
}
