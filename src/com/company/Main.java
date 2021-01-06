package com.company;

public class Main {

    public static void main(String[] args) {

        try (DbServer currSer = DbServer.run()) {

        } catch (Exception err){

            err.printStackTrace();
        }

        System.out.println("Successfully");
    }
}
