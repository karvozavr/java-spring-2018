package ru.spbau.mit.karvozavr.tictactoe.core;

import javafx.util.Pair;
import ru.spbau.mit.karvozavr.tictactoe.ui.layout.GameLayoutController;

public class PlayerAgent extends GameAgent {

    public PlayerAgent(CellType agentType, GameLayoutController layoutController) {
        super(agentType, layoutController);
    }

    @Override
    public String toString() {
        return agentType.toString();
    }

    @Override
    public void takeTurn(GameField field) {
        Pair<Integer, Integer> turn = layoutController.getNextTurn();
        if (turn != null)
            field.setCell(turn.getKey(), turn.getValue(), agentType);
    }
}
