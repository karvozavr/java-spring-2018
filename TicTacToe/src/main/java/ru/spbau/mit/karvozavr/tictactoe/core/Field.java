package ru.spbau.mit.karvozavr.tictactoe.core;

/**
 * Tic-Tac-Toe game field.
 */
public class Field {

    public static final int fieldSize = 3;
    private CellState[][] field = new CellState[fieldSize][fieldSize];

    public Field() {
        for (int raw = 0; raw < fieldSize; raw++)
            for (int col = 0; col < fieldSize; col++)
                field[raw][col] = CellState.EMPTY;
    }

    /**
     * Set the state of a single field cell.
     * @param raw raw position of the cell
     * @param col column position of the cell
     * @param state new state
     */
    public void setCell(int raw, int col, CellState state) {
        field[raw][col] = state;
    }

    /**
     * Get the state of a single field cell.
     * @param raw raw position of the cell
     * @param col column position of the cell
     * @return state of the cell
     */
    public CellState getCell(int raw, int col) {
        return field[raw][col];
    }
}
