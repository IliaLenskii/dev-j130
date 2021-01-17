package com.company;

import java.io.IOException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Client implements ISimpleChat {

    public Client() throws ChatException {

        String tmpLine;

        String host = SERVER_ADDRESS;
        int port = SERVER_PORT;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

/*
        try {
            System.out.println("Enter server ADDRESS (default "+ host +"): ");

            tmpLine = br.readLine().trim();

            if(!tmpLine.equals(""))
                host = tmpLine;

            System.out.println("Enter server PORT (default "+ port +"): ");

            tmpLine = br.readLine().trim();

            if(!tmpLine.equals(""))
                port = Integer.parseInt( tmpLine );

        } catch (IOException err){

            err.printStackTrace();
            return;
        }
 */

        try(Socket client = new Socket(host, port);
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());

        ) {

            System.out.println("Enter message to send to server: ");

            while(!client.isOutputShutdown()) {

                if(in.available() > 0) {

                    System.out.println( in.readUTF() );
                    Thread.sleep(100);
                }

                if(!br.ready()) {
                    Thread.sleep(100);
                    continue;
                }

                String clientCommand = br.readLine();

                if(clientCommand.equals("exit") || clientCommand.equals("quit")) {
                    break;
                }

                out.writeUTF(clientCommand);
                out.flush();
            }

            br.close();

        } catch (IOException | InterruptedException err) {

            err.printStackTrace();
        }
    }

    public void sendMessage(String message) throws ChatException {};

    public String getMessage() throws ChatException {
        return "";
    };

    public void server() throws ChatException {

    };

    public void client() throws ChatException {};

    public void close() throws ChatException {};

}
