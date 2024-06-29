package com.example.filesharing.client;

import com.example.filesharing.common.RequestInteractor;
import com.example.filesharing.common.RequestType;
import com.example.filesharing.common.FileTransferService;
import com.example.filesharing.common.FileTransferServiceImpl;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;

import static com.example.filesharing.server.ClientServiceThread.FILES_DELIMITER;

public class FTPClient implements Runnable {
    private FileTransferService fileTransfer = new FileTransferServiceImpl(); // класс управляющий передачей файлов
    RequestInteractor requestInteractor = new RequestInteractor(); // класс отправляющий запросы на сервер
    private static DataOutputStream out = null;
    private DataInputStream in = null;
    private final Socket socket;
    private FilesUpdater filesUpdater; // класс обновляющий UI при изменении на сервере
    private String downloadDirectory = "C:\\Users\\dude\\Downloads"; // директория для скачивания
    public FTPClient(Socket socket, FilesUpdater filesUpdater) {
        this.socket = socket;
        this.filesUpdater = filesUpdater;
    }
    public void sendFile(String path) throws IOException {
        requestInteractor.sendRequest(RequestType.UPLOAD_FILE, out);
        fileTransfer.send(path, out);
        out.flush();
    } // загрузить файл на сервер
    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            requestInteractor.sendRequest(RequestType.GET_ALL_FILES, out);
            String command;
            RequestType requestType;
            while (true) {
                if (in.available() > 0) {
                    command = in.readUTF();
                    System.out.println("received command " + command + " from server"); // log
                    requestType = RequestType.valueOf(command);
                    switch (requestType) {
                        case DISCONNECT -> {
                            socket.close();
                            return;
                        } // отсоединения сервера
                        case GET_ALL_FILES -> {
                            String str = in.readUTF();
                            System.out.println("received files [" + str + "]"); // log
                            String[] filesNames = str.split("\\" + FILES_DELIMITER);

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    filesUpdater.update(filesNames);
                                }
                            });

                            break;
                        } // получит список файлов доступных для скачивания
                        case DOWNLOAD_FILE -> {
                            try {
                                fileTransfer.receive(downloadDirectory, in);
                            } catch (IOException e) {
                                System.out.println("ERROR fileTransfer.receive");
                            }
                            break;
                        } // скачать файл с сервера
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // обработка запросов от сервера
    public void receiveFile(String fileName) throws IOException {
        requestInteractor.sendRequest(RequestType.DOWNLOAD_FILE, out);
        out.writeUTF(fileName);
    } // скачать файл с сервера
}

