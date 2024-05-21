package com.example.filesharing.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface FileTransferService {
    /**
     * Загружает файл на сервер
     *
     * @param directoryToSave путь до директории
     * @param in поток данных файла
     */
    void receive(String directoryToSave, DataInputStream in) throws IOException;

    /**
     * Скачивает файл с сервера
     *
     * @param path путь до файла
     * @param out поток данных файла
     */
     void send(String path, DataOutputStream out)throws IOException;
}
