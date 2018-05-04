package ru.spbau.mit.karvozavr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class.
 */
public class Main extends Application {

    private static int fieldSize = 5;
    private static final double windowSize = 800.0;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainLayout.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.initializeLayout(fieldSize, windowSize);

        Scene mainScene = new Scene(root, windowSize, windowSize);
        stage.setTitle("Pairs");
        stage.setMinHeight(windowSize);
        stage.setMinWidth(windowSize);
        stage.setScene(mainScene);
        stage.show();
    }

    public static void main(String[] args) {
        if (args.length >= 1) {
            fieldSize = Integer.parseInt(args[0]);
            if (fieldSize * fieldSize % 2 != 0) {
                System.err.println("Enter valid field size.");
                System.exit(1);
            }
            Application.launch(Main.class, args);
        } else {
            System.err.println("Provide field size argument!");
            System.exit(1);
        }
    }
}
