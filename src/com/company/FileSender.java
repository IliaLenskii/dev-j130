package com.company;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;

public class FileSender implements AutoCloseable {

    BufferedReader fileStream;

    public FileSender(String fileName) {
        Path path = Path.of(fileName);
        File file = path.toFile();

        if(file.isFile() == false || file.exists() == false) {
            System.out.println("The "+ fileName +" isn't file or doesn't exists");
            return;
        }

        try {

            fileStream = new BufferedReader(new FileReader(file));

        } catch(FileNotFoundException err) {

            err.printStackTrace();
            return;
        }

        try(Socket client = new Socket("localhost", FileReceiver.SERVER_PORT);
            DataOutputStream outgoing = new DataOutputStream(client.getOutputStream());
            BufferedReader bfRead = new BufferedReader(fileStream)) {

            String lineStr;

            while((lineStr = bfRead.readLine()) != null) {

                outgoing.writeBytes(lineStr +'\n');
            }

        } catch (IOException err) {

            err.printStackTrace();
        }

    }

    @Override
    public void close() throws Exception {

        if(fileStream != null)
            fileStream.close();
    }
}
