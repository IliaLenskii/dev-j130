package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

public class SimpleChat implements ISimpleChat {

    //public static final String SERVER_ADDRESS = "127.0.0.1";

    private ISimpleChat server;

    public SimpleChat() {
    }

    private SimpleChat(Boolean serverWillStart) {
    }

    @Override
    public void client() throws ChatException {

        try(Socket client = new Socket("localhost", SERVER_PORT);
            DataOutputStream outgoing = new DataOutputStream(client.getOutputStream());
            BufferedReader bfRead = new BufferedReader(fileStream)) {

            /*
            String lineStr;

            while((lineStr = bfRead.readLine()) != null) {

                outgoing.writeBytes(lineStr +'\n');
            }
            */

        } catch (IOException err) {

            err.printStackTrace();
        }

        System.out.println("Client stated");
    }

    @Override
    public void server() throws ChatException {

        if(server != null)
            return;

        server = new SimpleChat(true);

        try (ServerSocket ss = new ServerSocket(SERVER_PORT);
            Socket s = ss.accept();
            InputStream in = s.getInputStream();
            OutputStream out = s.getOutputStream()) {

            System.out.println("Sever stated");

            //byte[] buf = new byte[BUFFER_SIZE];

            /*
            // Имя файла приходит в виде сериализованной строки
            int n = in.read(buf);
            File file = createFile(new String(buf, 0, n));
            try (FileOutputStream fos = new FileOutputStream(file)) {
                while (true) {
                    n = in.read(buf);
                    // В конце файла/потока метод read() возвращает -1 (EOF).
                    if (n < 0) {
                        break;
                    }
                    fos.write(buf, 0, n);
                }
            }
             */

            //out.write("Transfer file finished.".getBytes());


        } catch (IOException e) {
            System.err.println("Error #1: " + e.getMessage());
        }
    }

    @Override
    public String getMessage() throws ChatException {

        return "_ok";
    }

    @Override
    public void sendMessage(String message) throws ChatException {

    }

    @Override
    public void close() throws ChatException {

    }
}
