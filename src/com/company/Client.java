package com.company;

import java.io.IOException;

import java.io.*;
import java.net.Socket;


public class Client implements ISimpleChat {

    private InputStream rawIn;
    private OutputStream rawOut;

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

        try(Socket client = new Socket(host, port)) {

            rawIn = client.getInputStream();
            rawOut = client.getOutputStream();

            System.out.println("Enter message to send to server: ");

            while(!client.isOutputShutdown()) {

                if(rawIn.available() > 0) {

                    System.out.println( getMessage() );

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

                sendMessage( clientCommand );
            }

            br.close();

        } catch (IOException | InterruptedException err) {

            err.printStackTrace();
        }
    }

    public void sendMessage(String message) throws ChatException {

        try {

            DataOutputStream out = new DataOutputStream(rawOut);

            out.writeUTF(message);
            out.flush();
        } catch (IOException err) {

            throw new ChatException(err);
        }
    };

    public String getMessage() throws ChatException {

        try {

            DataInputStream in = new DataInputStream(rawIn);

            return in.readUTF();
        } catch (IOException err) {

            throw new ChatException(err);
        }
    };

    public void close() throws ChatException {};
}
