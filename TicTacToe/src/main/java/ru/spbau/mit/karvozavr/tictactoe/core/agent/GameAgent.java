package ru.spbau.mit.karvozavr.tictactoe.core.agent;

import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.core.GameField;
import ru.spbau.mit.karvozavr.tictactoe.ui.layout.GameLayoutController;

import java.io.Serializable;

/**
 * Tic-Tac-Toe game agent.
 */
public abstract class GameAgent {

    protected final CellType agentType;
    protected final GameLayoutController layoutController;

    /**
     * Constructs game agent of type and layoutController.
     *
     * @param agentType        {@link CellType} of player X or O
     * @param layoutController {@link GameLayoutController} instance
     */
    public GameAgent(CellType agentType, GameLayoutController layoutController) {
        this.agentType = agentType;
        this.layoutController = layoutController;
    }

    /**
     * Returns string representation of player type.
     *
     * @return string representation of player type
     */
    public abstract String getTypeName();

    /**
     * Performs next turn on given field.
     *
     * @param field tic-tac-toe game field
     */
    public abstract void takeTurn(GameField field);
}
