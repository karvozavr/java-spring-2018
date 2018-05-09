package ru.spbau.mit.karvozavr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.spbau.mit.karvozavr.ftp_client.ui.MainLayoutController;

/**
 * Main application class.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainLayout.fxml"));
        Parent root = loader.load();
        MainLayoutController controller = loader.getController();

        Scene mainScene = new Scene(root, 800, 600);
        stage.setTitle("FTP Client");
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setScene(mainScene);
        stage.show();

        controller.initialize(mainScene.getWindow());
    }

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}