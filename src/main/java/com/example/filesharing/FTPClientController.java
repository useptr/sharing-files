package com.example.filesharing;

import com.example.filesharing.client.FTPClient;
import com.example.filesharing.client.FilesUpdater;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FTPClientController {
    @FXML
    private Label filePathLabel; // путь для фала загрузки
    @FXML
    private TextField filenameTextField; // имя файла для скачивания
    @FXML
    private ListView filesListView; // список доступных файлов для скачивания
    private FTPClient client = null; // класс для обработки запросов клиента и сервера
    private FilesUpdater filesUpdater = null; // класс для обновления списка файлов в UI
    private FileChooser fileChooser = new FileChooser(); // класс для выбора фала в проводнике
    @FXML
    private void initialize() {
        filesUpdater = new FilesUpdater(filesListView);
        fileChooser.setTitle("Выберите файл");
    }
    @FXML
    protected void downloadBtnClick() {
        try {
            String fileName = filenameTextField.getText();
            client.receiveFile(fileName);
            System.out.println("send command download file ["+fileName+"] to server"); // log
        } catch (IOException e) {
            System.out.println("ERROR downloadBtnClick"); // log
        }
    } // событие скачивания файла
    @FXML
    protected void chooseFileBtnClick(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null)
            filePathLabel.setText(selectedFile.getAbsolutePath());
    } // событие выбора файла
    @FXML
    protected void uploadBtnClick() {
        String absolutePath = filePathLabel.getText();
        try {
            client.sendFile(absolutePath);
            System.out.println("upload file ["+absolutePath+"]"); // log
        } catch (IOException e) {
        }
    } // событие загрузки файла на сервер
    public void setFTPClient(Socket socket) {
        client = new FTPClient(socket,filesUpdater);
        new Thread(client).start();
    } // установка обработчика запросов для соединения
}
