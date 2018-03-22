package ru.spbau.mit.karvozavr.tictactoe.core.agent;

import ru.spbau.mit.karvozavr.tictactoe.core.GameField;
import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.ui.layout.GameLayoutController;

import java.util.Arrays;
import java.util.List;

public class SmartBotAgent extends GameAgent {

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

        int turn = findBestTurn(field);
        int turnRow = turn / 3;
        int turnCol = turn % 3;
        field.setCell(turnRow, turnCol, agentType);

        layoutController.onTurnEnd();
    }

    private int findBestTurn(GameField field) {
        {
            int turn = getPriorityTurn(field, agentType);

            if (turn != -1)
                return turn;

            turn = getPriorityTurn(field, opponentType);

            if (turn != -1)
                return turn;
        }

        for (int turn : interests) {
            int turnRow = turn / 3;
            int turnCol = turn % 3;
            if (field.getCell(turnRow, turnCol) == CellType.EMPTY)
                return turn;
        }

        for (int turn = 0; turn < 9; turn++) {
            int turnRow = turn / 3;
            int turnCol = turn % 3;
            if (field.getCell(turnRow, turnCol) == CellType.EMPTY)
                return turn;
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
