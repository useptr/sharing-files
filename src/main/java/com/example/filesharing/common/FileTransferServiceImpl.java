package com.example.filesharing.common;

import java.io.*;

public class FileTransferServiceImpl implements FileTransferService{
    @Override
    public void receive(String directoryToSave, DataInputStream in) throws IOException {
        String fileName = in.readUTF();
        long fileSize = in.readLong();

        FileOutputStream fos = new FileOutputStream(directoryToSave + File.separator + fileName);
        byte[] buffer = new byte[4096];
        int read;
        long totalRead = 0;

        while ((read = in.read(buffer, 0, buffer.length)) > 0) {
            fos.write(buffer, 0, read);
            totalRead += read;
            if (totalRead >= fileSize) break;
        }
        fos.close();
        System.out.println("receive " + fileName); // log
    }
    @Override
    public void send(String path, DataOutputStream out) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(path);
            File file = new File(path);

            out.writeUTF(file.getName());
            out.writeLong(file.length());

            byte[] buffer = new byte[1024*2];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR not found " + path); // log
        }
    }
}
