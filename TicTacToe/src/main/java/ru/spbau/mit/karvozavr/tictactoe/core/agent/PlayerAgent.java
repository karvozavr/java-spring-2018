package ru.spbau.mit.karvozavr.tictactoe.core.agent;

import javafx.util.Pair;
import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.core.GameField;
import ru.spbau.mit.karvozavr.tictactoe.ui.layout.GameLayoutController;

public class PlayerAgent extends GameAgent {

    public PlayerAgent(CellType agentType, GameLayoutController layoutController) {
        super(agentType, layoutController);
    }

    @Override
    public String toString() {
        return "Human " + agentType;
    }

    @Override
    public void takeTurn(GameField field) {
        Pair<Integer, Integer> turn = layoutController.getNextTurn();
        if (turn != null)
            field.setCell(turn.getKey(), turn.getValue(), agentType);
    }
}
