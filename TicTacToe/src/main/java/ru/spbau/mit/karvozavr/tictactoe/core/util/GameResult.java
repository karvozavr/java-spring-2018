package ru.spbau.mit.karvozavr.tictactoe.core.util;

import java.io.Serializable;

public enum GameResult {
    X_WIN, O_WIN, DRAW, NOT_FINISHED;

    @Override
    public String toString() {
        switch (this) {
            case X_WIN:
                return "X won";
            case O_WIN:
                return "O won";
            case DRAW:
                return "Draw";
            default:
                return "Game not finished!";
        }
    }
}
