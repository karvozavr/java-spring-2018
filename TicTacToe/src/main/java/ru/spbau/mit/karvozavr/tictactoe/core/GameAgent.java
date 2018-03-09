package ru.spbau.mit.karvozavr.tictactoe.core;

public abstract class GameAgent {

    protected final CellType agentType;

    public GameAgent(CellType agentType) {
        this.agentType = agentType;
    }

    public abstract void takeTurn(GameField field);
}
