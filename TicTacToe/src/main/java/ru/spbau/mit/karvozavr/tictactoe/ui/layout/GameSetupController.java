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

public class GameSetupController implements Initializable {

    private static final ObservableList<String> agents =
        FXCollections.observableArrayList(GameAgentFactory.gameAgentTypes.keySet());

    @FXML
    public ChoiceBox<String> playerX;
    @FXML
    public ChoiceBox<String> playerO;

    private MainMenuLayoutController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerX.setItems(agents);
        playerX.setValue(agents.get(0));
        playerO.setItems(agents);
        playerO.setValue(agents.get(0));
    }

    public void injectMainController(MainMenuLayoutController mainController) {
        this.mainController = mainController;
    }

    public void createGameSetup(ActionEvent actionEvent) {
        mainController.onGameSetupReady(playerX.getValue(), playerO.getValue());
    }
}
