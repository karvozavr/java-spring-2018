package ru.spbau.mit.karvozavr.ftp_client.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    public void initialize(Window window) {
        this.window = window;
    }


    @FXML
    private void connect(ActionEvent actionEvent) {
        int port = 22334;

        try {
            port = Integer.parseInt(serverPort.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // TODO Show error
            return;
        }

        currentDirectory = "";
        ftpClient = new FTPClient(new InetSocketAddress(serverAddress.getText(), port));
        updateDirectoryView(currentDirectory);
        directories.clear();
    }

    private void updateDirectoryView(String directory) {
        currentDirectoryLabel.setText(directory);
        try {
            List<FileInfo> files = ftpClient.list(directory);
            directoryView.setItems(FXCollections.observableList(files));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void keyPressed(KeyEvent keyEvent) {
        System.out.println("Key event");
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onElementAction(directoryView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            onElementAction(directoryView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void onElementAction(FileInfo element) {
        if (element.getIsDirectory()) {
            directories.push(currentDirectory);
            currentDirectory = concatenatePaths(currentDirectory, element.getName());
            updateDirectoryView(currentDirectory);
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            File fileToSave = fileChooser.showSaveDialog(window);
            if (fileToSave != null) {
                try {
                    ftpClient.save(concatenatePaths(currentDirectory, element.getName()), fileToSave);
                } catch (IOException e) {
                    e.printStackTrace();
                    // TODO error
                }
            }
        }
    }

    @FXML
    private void toParentDirectory() {
        if (!directories.empty()) {
            currentDirectory = directories.pop();
            updateDirectoryView(currentDirectory);
        }
    }

    private String concatenatePaths(String path1, String path2) {
        return path1 + "/" + path2;
    }
}
