package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server extends Thread implements ISimpleChat  {

    private static HashSet<Server> serverList = new HashSet<>();

    private Socket socket;

    private InputStream rawIn;
    private OutputStream rawOut;

    public Server() throws ChatException {

        try (ServerSocket ss = new ServerSocket(SERVER_PORT)) {

            while (true) {

                Socket socket = ss.accept();

                serverList.add( new Server(socket) );
            }

        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    private Server(Socket socket) throws IOException {
        this.socket = socket;

        rawIn = socket.getInputStream();
        rawOut = socket.getOutputStream();

        start();
    }

    @Override
    public void run() {

        DataInputStream in = new DataInputStream(rawIn);
        DataOutputStream out = new DataOutputStream(rawOut);

        try {

            while(!socket.isClosed()) {

                if(rawIn.available() < 1) {

                    Thread.sleep(50);
                    continue;
                }

                String msg = in.readUTF();

                for(Server itm : serverList) {

                    if(this.hashCode() == itm.hashCode())
                        continue;

                    try {

                        itm.sendMessage( msg );

                    }  catch (ChatException e) {
                        e.printStackTrace();
                    }
                }

                //System.out.println( msg );
            }

        } catch (IOException | InterruptedException err) {
            err.printStackTrace();
        }
    }

    public void sendMessage(String message) throws ChatException {
        DataOutputStream out = new DataOutputStream(rawOut);

        try {

            out.writeUTF(message);
            out.flush();
        } catch (IOException err) {
            err.printStackTrace();
        }
    };

    public String getMessage() throws ChatException {
        return "";
    };

    public void close() throws ChatException {};
}
