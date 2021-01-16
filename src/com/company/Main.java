package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Will App start as server? (default is no) [no/yes]:");

        Scanner options = new Scanner(System.in);
        String line1 = options.nextLine();

        ISimpleChat ServerOrChat = new SimpleChat();

        try {

            if(line1.equals("yes"))
                ServerOrChat.server();
            else
                ServerOrChat.client();

        } catch (ChatException err) {

            err.printStackTrace();
        }


        //new FileReceiver().run();

        System.out.println("Successfully");
    }
}