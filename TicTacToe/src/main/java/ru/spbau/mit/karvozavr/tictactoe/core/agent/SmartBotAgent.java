package ru.spbau.mit.karvozavr.tictactoe.core.agent;

import ru.spbau.mit.karvozavr.tictactoe.core.GameField;
import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.ui.layout.GameLayoutController;

import java.util.Arrays;
import java.util.List;

/**
 * {@link GameAgent} instance: smart bot with heuristic based behaviour.
 */
public class SmartBotAgent extends GameAgent {

    /**
     * Turn data class.
     */
    private class Turn {
        public int row;
        public int col;

        public Turn(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static final List<Integer> interests = Arrays.asList(4, 0, 2, 6, 8);
    private final CellType opponentType;

    public SmartBotAgent(CellType agentType, GameLayoutController layoutController) {
        super(agentType, layoutController);

        if (agentType == CellType.X)
            opponentType = CellType.O;
        else
            opponentType = CellType.X;
    }

    @Override
    public String getTypeName() {
        return agentType.toString();
    }

    @Override
    public String toString() {
        return "Smart bot";
    }

    @Override
    public void takeTurn(GameField field) {
        layoutController.onTurnStart();
        Turn turn = findBestTurn(field);
        field.setCell(turn.row, turn.col, agentType);
        layoutController.onTurnEnd();
    }

    /**
     * Finds the best turn due to heuristics.
     *
     * @param field game field
     * @return turn
     */
    private Turn findBestTurn(GameField field) {
        {
            int turn = getPriorityTurn(field, agentType);

            if (turn != -1)
                return new Turn(turn / 3, turn % 3);

            turn = getPriorityTurn(field, opponentType);

            if (turn != -1)
                return new Turn(turn / 3, turn % 3);
        }

        for (int turn : interests) {
            int turnRow = turn / 3;
            int turnCol = turn % 3;
            if (field.getCell(turnRow, turnCol) == CellType.EMPTY)
                return new Turn(turn / 3, turn % 3);
        }

        for (int turn = 0; turn < 9; turn++) {
            int turnRow = turn / 3;
            int turnCol = turn % 3;
            if (field.getCell(turnRow, turnCol) == CellType.EMPTY)
                return new Turn(turn / 3, turn % 3);
        }

        throw new IllegalStateException("Bot logic error!");
    }

    private int getPriorityTurn(GameField field, CellType type) {
        for (int turn = 0; turn < 9; ++turn) {
            int turnRow = turn / 3;
            int turnCol = turn % 3;
            if (field.getCell(turnRow, turnCol) == CellType.EMPTY && existDoubleSequence(turn, field, type))
                return turn;
        }

        return -1;
    }

    private boolean existDoubleSequence(int turn, GameField field, CellType type) {
        int turnRow = turn / 3;
        int turnCol = turn % 3;
        boolean result = false;

        // check horizontal
        {
            int count = 0;
            for (int i = 0; i < 3; ++i)
                if (field.getCell(turnRow, i) == type)
                    count++;
            result |= count == 2;
        }

        // check vertical
        {
            int count = 0;
            for (int i = 0; i < 3; ++i)
                if (field.getCell(i, turnCol) == type)
                    count++;
            result |= count == 2;
        }

        // check diagonal
        if (turnRow == turnCol) {
            int count = 0;
            for (int i = 0; i < 3; ++i)
                if (field.getCell(i, i) == type)
                    count++;
            result |= count == 2;
        }

        if (turnRow == 2 - turnCol) {
            int count = 0;
            for (int i = 0; i < 3; ++i)
                if (field.getCell(i, 2 - i) == type)
                    count++;
            result |= count == 2;
        }

        return result;
    }
}
