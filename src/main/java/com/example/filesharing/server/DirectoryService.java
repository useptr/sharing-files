package com.example.filesharing.server;

import java.io.IOException;
import java.util.List;

public interface DirectoryService {
    /**
     * Возвращает список имен всех файлов в указанной директории.
     *
     * @param directoryPath путь к директории
     * @return список имен файлов
     */
    List<String> files(String directoryPath) throws IOException;
}
