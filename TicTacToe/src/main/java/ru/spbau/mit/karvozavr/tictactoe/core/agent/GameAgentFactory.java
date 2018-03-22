package ru.spbau.mit.karvozavr.tictactoe.core.agent;

import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.ui.layout.GameLayoutController;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class GameAgentFactory {

    public static final Map<String, Class<? extends GameAgent>> gameAgentTypes;

    static {
        Map<String, Class<? extends GameAgent>> tempMap = new TreeMap<>();
        tempMap.put("Human", PlayerAgent.class);
        tempMap.put("Random bot", RandomBotAgent.class);
        gameAgentTypes = Collections.unmodifiableMap(tempMap);
    }

    public static PlayerAgent playerAgent(CellType type, GameLayoutController layoutController) {
        return new PlayerAgent(type, layoutController);
    }

    public static RandomBotAgent randomBotAgent(CellType type, GameLayoutController layoutController) {
        return new RandomBotAgent(type, layoutController);
    }

    public static GameAgent agent(String agentName, CellType type, GameLayoutController layoutController) {
        try {
            return gameAgentTypes
                .get(agentName)
                .getDeclaredConstructor(CellType.class, GameLayoutController.class)
                .newInstance(type, layoutController);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException("Invalid agent instantiation!", e);
        }
    }
}
