package com.example.filesharing.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class FTPServer {
    private final int ANY_AVAILABLE_PORT = 0; // аргумент конструктора ServerSocket для получения любого свободного порта
    private static Set<ClientServiceThread> clients = new HashSet<>(); // все текущие подключения
    public FTPServer() {
        try (ServerSocket socket = new ServerSocket(ANY_AVAILABLE_PORT)) {
            InetAddress ip = socket.getInetAddress();
            int port = socket.getLocalPort();
            System.out.println("Server running " + ip.getHostAddress() + ':' + port); // log
            while (true) {
                Socket clientSocket = socket.accept();

                ip = clientSocket.getInetAddress();
                port = socket.getLocalPort();
                System.out.println("New connection " + ip.getHostAddress() + ':' + port); // log

                ClientServiceThread clientHandler = new ClientServiceThread(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void notifyFilesChanged() {
        for (ClientServiceThread client : clients) {
            try {
                client.sendRequestFilesChanged();
                System.out.println("send all files to " + client.ip() + ':' + client.port()); // log
            } catch (IOException e) {
            }
        }

    } // уведомить клиентов об изменениях на сервере (например, загрузка нового файла)
    public void remove(ClientServiceThread client) {
        clients.remove(client);
    }
}
