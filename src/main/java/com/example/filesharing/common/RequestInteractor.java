package com.example.filesharing.common;

import java.io.DataOutputStream;
import java.io.IOException;

public class RequestInteractor {
    public void sendRequest(RequestType request, DataOutputStream out) throws IOException {
        out.writeUTF(request.requestType());
    }
}
