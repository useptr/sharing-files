package com.example.filesharing.server;

import com.example.filesharing.common.FileTransferService;
import com.example.filesharing.common.FileTransferServiceImpl;
import com.example.filesharing.common.RequestInteractor;
import com.example.filesharing.common.RequestType;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class ClientServiceThread implements Runnable {
    public static final String PATH_TO_FILES = "files/";
    public static final String FILES_DELIMITER = "|";
    private final Socket socket;
    private final FTPServer server;
    private DataOutputStream  out;
    private DataInputStream in;
    private final RequestInteractor requestInteractor = new RequestInteractor();

    private InetAddress ip = null;
    private int port = -1;

    private final FileTransferService fileTransfer = new FileTransferServiceImpl();
    private final DirectoryService directoryService = new DirectoryServiceImpl();
    public ClientServiceThread(Socket socket, FTPServer server) {
        this.socket = socket;
        this.server = server;
    }
    public String ip() {
        return ip.getHostAddress();
    }
    public int port() {
        return port;
    }

    @Override
    public void run() {
        ip = socket.getInetAddress();
        port = socket.getLocalPort();
        try {
            in = new DataInputStream(this.socket.getInputStream());
            out = new DataOutputStream (this.socket.getOutputStream());
            String command;
            RequestType requestType;
            while (true) {
                if (in.available() > 0) {
                    command = in.readUTF();
                        System.out.println("received command "+command+" from " + ip() + ':' + port); // log
                    requestType = RequestType.valueOf(command);
                    switch (requestType) {
                        case DISCONNECT -> {
                            closeConnection();
                            return;
                        } // отсоединения клиента
                        case DOWNLOAD_FILE -> {
                            String fileName = in.readUTF();
                            requestInteractor.sendRequest(RequestType.DOWNLOAD_FILE, out);
                            System.out.println("send command " + RequestType.DOWNLOAD_FILE + " to " + ip() + ':' + port); // log
                            fileTransfer.send(PATH_TO_FILES + File.separator + fileName, out);
                            out.flush();
                            System.out.println("send file [" + fileName + "] to " + ip() + ':' + port); // log
                        } // скачать файл с сервера
                        case UPLOAD_FILE -> {
                            fileTransfer.receive(PATH_TO_FILES, in);
                            server.notifyFilesChanged();
                        } // загрузить файл на сервер
                        case GET_ALL_FILES -> {
                            sendRequestFilesChanged();
                            System.out.println("send all files to " + ip() + ':' + port); // log
                        } // получит список файлов доступных для скачивания
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Disconnect " + ip() + ':' + port);
            closeConnection();
            server.remove(this);
        }
    }
    public void sendRequestFilesChanged() throws IOException {
        List<String> files = directoryService.files(PATH_TO_FILES);
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < files.size(); ++i) {
            str.append(files.get(i));
            if (i != files.size()-1)
                str.append(FILES_DELIMITER);
        }
        requestInteractor.sendRequest(RequestType.GET_ALL_FILES, out);
        out.writeUTF(str.toString());
        out.flush();
    }

    private void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
