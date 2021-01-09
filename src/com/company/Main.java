package com.company;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {

        try (FileSender sv = new FileSender("1.txt")) {

        } catch (FileNotFoundException err) {

            err.printStackTrace();

        } catch (Exception err){

            err.printStackTrace();
        }

        System.out.println("Successfully");
    }
}
