package ru.spbau.mit.karvozavr.tictactoe.core.agent;

import ru.spbau.mit.karvozavr.tictactoe.core.util.CellType;
import ru.spbau.mit.karvozavr.tictactoe.ui.layout.GameLayoutController;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Factory to create Tic-Tac-Toe game agents.
 */
public class GameAgentFactory {

    /**
     * Map: agent type string -> agent type class.
     */
    public static final Map<String, Class<? extends GameAgent>> gameAgentTypes;

    static {
        Map<String, Class<? extends GameAgent>> tempMap = new TreeMap<>();
        tempMap.put("Human", PlayerAgent.class);
        tempMap.put("Random bot", RandomBotAgent.class);
        tempMap.put("Smart bot", SmartBotAgent.class);
        gameAgentTypes = Collections.unmodifiableMap(tempMap);
    }

    /**
     * Creates agent of given type.
     *
     * @param agentType        string: agent type
     * @param playerType       player type X or O
     * @param layoutController game layout controller to bind agent with
     * @return new agent
     */
    public static GameAgent agent(String agentType, CellType playerType, GameLayoutController layoutController) {
        try {
            return gameAgentTypes
                .get(agentType)
                .getDeclaredConstructor(CellType.class, GameLayoutController.class)
                .newInstance(playerType, layoutController);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException("Invalid agent instantiation!", e);
        }
    }
}
