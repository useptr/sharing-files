package com.example.filesharing.client;

import javafx.scene.control.ListView;

public class FilesUpdater implements FilesUpdateRequestHandler {
    private ListView filesView;
    public FilesUpdater(ListView filesView) {
        this.filesView = filesView;
    }
    @Override
    public void update(String[] filesNames) {
        filesView.getItems().clear();
        for (String fileName : filesNames) {
            filesView.getItems().add(fileName);
        }
    }
}
