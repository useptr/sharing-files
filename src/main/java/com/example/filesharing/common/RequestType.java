package com.example.filesharing.common;

public enum  RequestType {

    DISCONNECT("DISCONNECT"),
    UPLOAD_FILE("UPLOAD_FILE"),
    GET_ALL_FILES("GET_ALL_FILES"),
    DOWNLOAD_FILE("DOWNLOAD_FILE");

    private final String type;

    RequestType(String type) {
        this.type = type;
    }
    public String requestType() {
        return type;
    }
}
