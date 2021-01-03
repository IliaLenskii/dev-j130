package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {

    private static final String TABLE_AUTHORS = "CREATE TABLE IF NOT EXISTS `Authors` ("+
            "`id` INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "`name` VARCHAR(64) NOT NULL,"+
            "`info` VARCHAR(255)"+
            ")";

    private static final String[] TABLE_AUTHORS_DATA = new String[]{
            "DELETE FROM `Authors`",

            "INSERT INTO `Authors` (`name`) VALUES ('Arnold Grey')",
            "INSERT INTO `Authors` (`name`, `info`) VALUES ('Tom Hawkins', 'new author')",
            "INSERT INTO `Authors` (`name`) VALUES ('Jim Beam')"
    };

    private static final String TABLE_DOCUMENTS = "CREATE TABLE IF NOT EXISTS `Documents` ("+
            "`id` INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "`name` VARCHAR(64) NOT NULL,"+
            "`info` TEXT,"+
            "`date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"+
            "`author` INTEGER NOT NULL,"+
            "FOREIGN KEY(author) REFERENCES Authors(id)"+
            ")";

    private static final String[] TABLE_DOCUMENTS_DATA = new String[]{
            "DELETE FROM `Documents`",

            "INSERT INTO `Documents` (`name`, `info`, `author`) VALUES ('Project plan', 'First content', (SELECT `id` FROM `Authors` WHERE `name` == 'Arnold Grey' LIMIT 1))",

            "INSERT INTO `Documents` (`name`, `info`, `author`) VALUES ('First report', 'Second content', (SELECT `id` FROM `Authors` WHERE `name` == 'Tom Hawkins' LIMIT 1))",
            "INSERT INTO `Documents` (`name`, `info`, `author`) VALUES ('Test result', 'Third content', (SELECT `id` FROM `Authors` WHERE `name` == 'Tom Hawkins' LIMIT 1))",

            "INSERT INTO `Documents` (`name`, `info`, `author`) VALUES ('Second report', 'Report content', (SELECT `id` FROM `Authors` WHERE `name` == 'Jim Beam' LIMIT 1))"
    };

    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;

        try {

            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException err) {

            err.printStackTrace();
            return;
        }

        try {

            connection = DriverManager.getConnection("jdbc:sqlite:test.db3");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch(SQLException err) {

            err.printStackTrace();
            return;
        }

        // First step
        try {

            // Tables create
            statement.execute(Main.TABLE_AUTHORS);
            statement.execute(Main.TABLE_DOCUMENTS);

            // Data for table Authors
            for(var i = 0; i < Main.TABLE_AUTHORS_DATA.length; ++i)
                statement.execute(Main.TABLE_AUTHORS_DATA[i]);

            // Data for table Documents
            for(var i = 0; i < Main.TABLE_DOCUMENTS_DATA.length; ++i)
                statement.execute(Main.TABLE_DOCUMENTS_DATA[i]);

        } catch(SQLException e) {

            e.printStackTrace();
            return;
        }

        // Second step
        try {

            // Update if the column is null
            statement.execute("UPDATE `Authors` SET `info` = \"No Data\" WHERE `info` IS NULL");


            // All doc by Tom Hawkins
            ResultSet  rs = statement.executeQuery("SELECT * FROM Documents WHERE `author` = (SELECT `id` FROM `Authors` WHERE `name` = 'Tom Hawkins')");

            while(rs.next()) {
                System.out.println("name: "+ rs.getString("name") +", info: "+ rs.getString("info"));
            }

            System.out.println("__________");

            // Look for doc with %report%
            ResultSet  rs2 = statement.executeQuery("SELECT `id`, `name`, (SELECT `name` FROM `Authors` WHERE Authors.`id` = `author`) as autName FROM Documents as D WHERE `name` LIKE '%report%'");

            while(rs2.next()) {
                System.out.println("id: "+ rs.getInt("id") +", name: "+ rs.getString("name")+", author: "+ rs.getString("autName"));
            }

        } catch(SQLException e) {

            e.printStackTrace();
            return;
        }

/*
4. Напишите оператор SELECT, который запрашивает идентификатор, название документа и его автора, при условии, что в названии документа есть слово report
 */
        System.out.println("Successfully");
    }
}
