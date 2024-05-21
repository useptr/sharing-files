package com.example.filesharing.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirectoryServiceImpl implements DirectoryService{
    @Override
    public List<String> files(String directoryPath) throws IOException {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile())
                    fileNames.add(listOfFiles[i].getName());
            }
        }
        return fileNames;
    }
}
