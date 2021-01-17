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
/*
public class SimpleChat extends Thread implements ISimpleChat {


    public void sendMessage(String message) throws ChatException {};

    public String getMessage() throws ChatException {
        return "";
    };

    public void server() throws ChatException {};

    public void client() throws ChatException {};

    public void close() throws ChatException {};
}
 */
