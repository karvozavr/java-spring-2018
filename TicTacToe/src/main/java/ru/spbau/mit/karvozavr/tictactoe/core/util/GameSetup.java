package ru.spbau.mit.karvozavr.tictactoe.core.util;

import ru.spbau.mit.karvozavr.tictactoe.GameStatisticsManager;
import ru.spbau.mit.karvozavr.tictactoe.core.agent.GameAgent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Single game settings and information.
 */
public class GameSetup {

    private final GameAgent gameAgentX;
    private final GameAgent gameAgentO;
    private GameResult gameResult = GameResult.NOT_FINISHED;
    private LocalDateTime date;

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

    public LocalDateTime getDate() {
        return date;
    }

    public void saveGameStatistics() {
        date = LocalDateTime.now();
        GameStatisticsManager.writeGame(this);
    }
}
