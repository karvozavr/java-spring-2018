package ru.spbau.mit.karvozavr.tictactoe.core.agent;

import ru.spbau.mit.karvozavr.tictactoe.core.CellType;
import ru.spbau.mit.karvozavr.tictactoe.core.GameField;
import ru.spbau.mit.karvozavr.tictactoe.ui.layout.GameLayoutController;

public abstract class GameAgent {

    protected final CellType agentType;
    protected final GameLayoutController layoutController;

    public GameAgent(CellType agentType, GameLayoutController layoutController) {
        this.agentType = agentType;
        this.layoutController = layoutController;
    }

    public abstract void takeTurn(GameField field);
}
