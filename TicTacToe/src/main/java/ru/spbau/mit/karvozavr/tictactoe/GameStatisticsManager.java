package ru.spbau.mit.karvozavr.tictactoe;

import com.sun.javafx.collections.ImmutableObservableList;
import ru.spbau.mit.karvozavr.tictactoe.core.util.GameSetup;

import java.io.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tic-Tac-Toe game statistics manager.
 */
public class GameStatisticsManager {

    private static List<GameStats> statistics;
    private static final String fileName = "tic-tac-toe-stats.bin";

    static {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            statistics = (List<GameStats>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            statistics = new ArrayList<>();
            flushStatistics();
        }
    }

    public static List<GameStats> getStatistics() {
        return Collections.unmodifiableList(statistics);
    }

    /**
     * Makes game info record.
     *
     * @param gameSetup game setup
     */
    public static void writeGame(GameSetup gameSetup) {
        statistics.add(new GameStats(
            gameSetup.getGameAgentX().toString(),
            gameSetup.getGameAgentO().toString(),
            gameSetup.getGameResult().toString(),
            gameSetup.getDate().format(DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault())))
        );
    }

    /**
     * Saves statistics to file.
     */
    private static void flushStatistics() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            objectOutputStream.writeObject(statistics);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save statistics!", e);
        }
    }

    /**
     * App termination callback.
     */
    public static void onAppStop() {
        flushStatistics();
    }

    /**
     * Single game statistics data class.
     */
    public static class GameStats implements Serializable {
        private final String playerX;
        private final String playerO;
        private final String gameResult;
        private final String dateTime;

        public GameStats(String playerX, String playerO, String gameResult, String dateTime) {
            this.playerX = playerX;
            this.playerO = playerO;
            this.gameResult = gameResult;
            this.dateTime = dateTime;
        }

        public String getPlayerX() {
            return playerX;
        }

        public String getPlayerO() {
            return playerO;
        }

        public String getGameResult() {
            return gameResult;
        }

        public String getDateTime() {
            return dateTime;
        }
    }
}
