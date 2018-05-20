package ru.spbau.mit.karvozavr.tictactoe.core.agent;

import ru.spbau.mit.karvozavr.tictactoe.core.GameField;
import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.ui.layout.GameLayoutController;

/**
 * {@link GameAgent} instance implementing real human player behaviour.
 */
public class PlayerAgent extends GameAgent {

    public PlayerAgent(CellType agentType, GameLayoutController layoutController) {
        super(agentType, layoutController);
    }

    @Override
    public String getTypeName() {
        return agentType.toString();
    }

    @Override
    public String toString() {
        return "Human";
    }

    @Override
    public void takeTurn(GameField field) {
        Integer turn = layoutController.getNextTurn();
        if (turn != null) {
            field.setCell(turn / 3, turn % 3, agentType);
        }
    }
}
