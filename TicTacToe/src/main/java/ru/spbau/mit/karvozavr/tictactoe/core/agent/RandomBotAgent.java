package ru.spbau.mit.karvozavr.tictactoe.core.agent;

import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.core.GameField;
import ru.spbau.mit.karvozavr.tictactoe.ui.layout.GameLayoutController;

import java.util.Random;

public class RandomBotAgent extends GameAgent {

    private final Random random = new Random();

    public RandomBotAgent(CellType agentType, GameLayoutController layoutController) {
        super(agentType, layoutController);
    }

    @Override
    public void takeTurn(GameField field) {
        layoutController.onTurnStart();

        boolean success = false;

        while (!success) {
            int turn = random.nextInt(9);

            if (field.getCell(turn / 3, turn % 3) == CellType.EMPTY) {
                field.setCell(turn / 3, turn % 3, agentType);
                success = true;
            }
        }

        layoutController.onTurnEnd();
    }
}
