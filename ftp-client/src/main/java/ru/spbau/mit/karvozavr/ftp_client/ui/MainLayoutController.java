package ru.spbau.mit.karvozavr.ftp_client.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import ru.spbau.mit.karvozavr.ftp_client.FTPClient;
import ru.spbau.mit.karvozavr.ftp_client.ui.util.FileInfo;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Stack;

/**
 * Main application layout controller.
 */
public class MainLayoutController {

    @FXML
    public TableView<FileInfo> directoryView;
    public TextField serverPort;
    public Label currentDirectoryLabel;
    @FXML
    private TextField serverAddress;

    private FTPClient ftpClient;
    private String currentDirectory = "";
    private Stack<String> directories = new Stack<>();
    private Window window;

    /**
     * Initializes controller.
     *
     * @param window window containing layout
     */
    public void initialize(Window window) {
        this.window = window;
    }

    /**
     * Connect to server button listener.
     *
     * @param actionEvent event
     */
    @FXML
    private void connect(ActionEvent actionEvent) {
        int port;

        try {
            port = Integer.parseInt(serverPort.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showError("Failed to connect to the server.\nCheck if address and port are correct.");
            return;
        }

        currentDirectory = "";
        ftpClient = new FTPClient(new InetSocketAddress(serverAddress.getText(), port));
        updateDirectoryView(currentDirectory);
        directories.clear();
    }

    /**
     * Updates directory view with given directory from server.
     *
     * @param directory directory name
     */
    private void updateDirectoryView(String directory) {
        currentDirectoryLabel.setText(directory);
        try {
            List<FileInfo> files = ftpClient.list(directory);
            directoryView.setItems(FXCollections.observableList(files));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * On entry double click event listener.
     *
     * @param mouseEvent event
     */
    @FXML
    private void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            onEntryAction(directoryView.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * On entry double clicked action.
     *
     * @param entry FileInfo entry
     */
    private void onEntryAction(FileInfo entry) {
        if (entry.getIsDirectory()) {
            directories.push(currentDirectory);
            currentDirectory = concatenatePaths(currentDirectory, entry.getName());
            updateDirectoryView(currentDirectory);
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            File fileToSave = fileChooser.showSaveDialog(window);
            if (fileToSave != null) {
                try {
                    ftpClient.save(concatenatePaths(currentDirectory, entry.getName()), fileToSave);
                } catch (IOException e) {
                    e.printStackTrace();
                    showError("Failed to download file.");
                }
            }
        }
    }

    /**
     * Update file view with parent directory from server.
     */
    @FXML
    private void toParentDirectory() {
        if (!directories.empty()) {
            currentDirectory = directories.pop();
            updateDirectoryView(currentDirectory);
        }
    }

    /**
     * Concatenate two paths.
     *
     * @param path1 path 1
     * @param path2 path 2
     * @return path 1 ++ path 2
     */
    private String concatenatePaths(String path1, String path2) {
        return path1 + "/" + path2;
    }

    /**
     * Show error dialog.
     * @param error error
     */
    private void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something gone wrong.");
        alert.setContentText(error);

        alert.showAndWait();
    }
}
