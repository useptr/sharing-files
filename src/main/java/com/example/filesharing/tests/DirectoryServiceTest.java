package com.example.filesharing.tests;

import com.example.filesharing.server.DirectoryService;
import com.example.filesharing.server.DirectoryServiceImpl;

import java.io.IOException;
import java.util.List;

import static com.example.filesharing.server.ClientServiceThread.PATH_TO_FILES;

public class DirectoryServiceTest {


    public static void main(String[] args)  throws IOException {
        DirectoryService service = new DirectoryServiceImpl();
        List<String> files = service.files(PATH_TO_FILES);
//        System.out.println(files);
    }
}
