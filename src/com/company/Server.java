package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/*
Определите класс SimpleChat, предназначенный для обмена текстовыми
сообщениями между двумя пользователями в сети. Класс должен реализовывать
интерфейс ISimpleChat. Класс может работать в двух режимах:

1. В режиме сервера приложение ожидает запроса на соединение от клиента;
2. В режиме клиента приложение прежде всего запрашивает у пользователя

адрес и порт сервера, а затем устанавливает соединение с сервером.
Инициатором завершения сеанса может выступать как клиент, так и сервер
приложения.
 */

public class Server extends Thread implements ISimpleChat  {

    private static HashSet<Server> serverList = new HashSet<>();

    private Socket socket;

    private InputStream rawIn;
    private OutputStream rawOut;

    public Server() throws ChatException {

        try (ServerSocket ss = new ServerSocket(SERVER_PORT)) {

            while (true) {

                Socket socket = ss.accept();

                //System.out.println("aad_client");

                serverList.add( new Server(socket) );
            }

        } catch (IOException err) { // | InterruptedException
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

        //System.out.println("RUN");
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

    //public void server() throws ChatException {};
    //public void client() throws ChatException {};

    public void close() throws ChatException {};
}

/*


public class SimpleChat implements ISimpleChat {

    private static HashSet<ISimpleChat> serverList;

    private Socket socket;

    private InputStream rawIn;
    private OutputStream rawOut;

    public SimpleChat() {}

    private SimpleChat(Socket socket) throws IOException {
        this.socket = socket;

        rawIn = socket.getInputStream();
        rawOut = socket.getOutputStream();

        DataInputStream in = new DataInputStream(rawIn);

        try {
            while(!socket.isClosed()) {

                if(rawIn.available() < 1) {

                    Thread.sleep(50);
                    continue;
                }

                String msg = in.readUTF();

                System.out.println( msg );
            }

        } catch (IOException | InterruptedException err) {
            err.printStackTrace();
        }
    }

    @Override
    public void client() throws ChatException {

        String tmpLine;

        String host = SERVER_ADDRESS;
        int port = SERVER_PORT;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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

        try(Socket client = new Socket(host, port);
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());

        ) {

            System.out.println("Enter message to send to server: ");

            while(!client.isOutputShutdown()) {

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

    @Override
    public void server() throws ChatException {

        try (ServerSocket ss = new ServerSocket(SERVER_PORT)) {

            while (true) {

                Socket socket = ss.accept();

                System.out.println("add client");

                serverList.add( new SimpleChat(socket) );
            }

        } catch (IOException err) { // | InterruptedException
            err.printStackTrace();
        }

        /*
        try (ServerSocket ss = new ServerSocket(SERVER_PORT);
            Socket client = ss.accept();
             InputStream rawIn = client.getInputStream();
             OutputStream rawOut = client.getOutputStream())
        {
            //System.out.println("Sever is waiting messages");

            DataInputStream in = new DataInputStream(rawIn);
            DataOutputStream out = new DataOutputStream(rawOut);

            //byte[] buf = new byte[BUFFER_SIZE];

            while(!client.isClosed()) {

                if(rawIn.available() < 1) {

                    Thread.sleep(50);
                    continue;
                }

                String msg = in.readUTF();

                System.out.println( msg );
            }

        } catch (IOException | InterruptedException err) {
            err.printStackTrace();
        }
    }

    @Override
    public String getMessage() throws ChatException {


    //private InputStream rawIn;
    //private OutputStream rawOut;

    //DataInputStream in = new DataInputStream(rawIn);


        return "_ok";
    }

    @Override
    public void sendMessage(String message) throws ChatException {

        DataOutputStream out = new DataOutputStream(rawOut);

        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException err) {

            err.printStackTrace();

            throw new ChatException(err);
        }
    }

    @Override
    public void close() throws ChatException {

    }
}

 */