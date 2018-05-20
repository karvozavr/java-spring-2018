package ru.spbau.mit.karvozavr.tictactoe.ui.layout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import ru.spbau.mit.karvozavr.tictactoe.core.agent.GameAgentFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Game setup layout controller.
 */
public class GameSetupLayoutController implements Initializable {

    /**
     * Agent selectors' content.
     */
    private static final ObservableList<String> agents =
        FXCollections.observableArrayList(GameAgentFactory.gameAgentTypes.keySet());

    @FXML
    private ChoiceBox<String> playerX;
    @FXML
    private ChoiceBox<String> playerO;

    private MainLayoutController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerX.setItems(agents);
        playerX.setValue(agents.get(0));
        playerO.setItems(agents);
        playerO.setValue(agents.get(0));
    }

    /**
     * Injects {@link MainLayoutController} dependency.
     *
     * @param mainController dependency object
     */
    public void injectMainController(MainLayoutController mainController) {
        this.mainController = mainController;
    }

    /**
     * Handles game setup user input.
     *
     * @param actionEvent event
     */
    public void createGameSetup(ActionEvent actionEvent) {
        mainController.onGameSetupReady(playerX.getValue(), playerO.getValue());
    }
}