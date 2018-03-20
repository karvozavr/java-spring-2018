package ru.spbau.mit.karvozavr.tictactoe.core.util;

import ru.spbau.mit.karvozavr.tictactoe.core.agent.GameAgent;

/**
 * Single game settings and information.
 */
public class GameSetup {

    private final GameAgent gameAgentX;
    private final GameAgent gameAgentO;
    private GameResult gameResult = GameResult.NOT_FINISHED;

    public GameSetup(GameAgent gameAgentX, GameAgent gameAgentO) {
        this.gameAgentX = gameAgentX;
        this.gameAgentO = gameAgentO;
    }

    public GameAgent getGameAgentX() {
        return gameAgentX;
    }

    public GameAgent getGameAgentO() {
        return gameAgentO;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }
}
