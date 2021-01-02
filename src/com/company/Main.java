package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;

        try {

            //Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:test.db3");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch(SQLException e) {

            e.printStackTrace();
            return;
        }

        try {

            statement.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'phone' INT);");

        } catch(SQLException e) {

            e.printStackTrace();
            return;
        }


        System.out.println("__END");
    }
}
