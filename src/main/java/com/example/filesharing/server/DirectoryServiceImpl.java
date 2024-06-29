package com.example.filesharing.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryServiceImpl implements DirectoryService{
    @Override
    public List<String> files(String directoryPath) {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile())
                    fileNames.add(listOfFile.getName());
            }
        }
        return fileNames;
    }
}
