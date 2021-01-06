package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbServer implements IDbService {

    /*
    * Автор не стал усложнять логику какой коллекцией для хранения переменных ниже,
    * так как это однопользовательское приложение.
    * Но если в будущем в случае много пользовательского приложения,
    * обязательно будет хранить данные in HashSet
     */
    public static Connection connection = null;
    public static Statement statement = null;

    private static DbServer dbServ = null;

    private DbServer() {

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

        // Tables create and data for table Authors
        Authors.prepareSQLData(statement);
        Documents.prepareSQLData(statement);

/*
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

        */
    }

    public static DbServer run() {

        if(DbServer.dbServ != null)
            return DbServer.dbServ;

        DbServer.dbServ = new DbServer();

        return DbServer.dbServ;
    }

    @Override
    public boolean addAuthor(Authors author) throws DocumentException {

        return false;
    }

    @Override
    public boolean addDocument(Documents doc, Authors author) throws  DocumentException {

        return false;
    }

    @Override
    public Documents[] findDocumentByAuthor(Authors author) throws  DocumentException {

        return null;
    }

    @Override
    public Documents[] findDocumentByContent(String content) throws DocumentException {

        return null;
    }

    @Override
    public boolean deleteAuthor(Authors author) throws DocumentException {

        return false;
    }

    @Override
    public boolean deleteAuthor(int id) throws DocumentException {

        return false;
    }

    /*
    public static Statement getStatement() {

        if(statement != null) {

            try {

                if(statement.isClosed() != false)
                    return statement;
            } catch ( SQLException err){

                err.printStackTrace();
                return null;
            }
        }

        return null;
    }
     */

    @Override
    public void close() {

        if(statement != null) {

            try {
                if (statement.isClosed())
                    statement.close();

            } catch ( SQLException err){}
        }

        if(connection != null) {

            try {
                if (connection.isClosed())
                    connection.close();

            } catch ( SQLException err){}
        }

        dbServ = null;

        System.out.println("__close");
    }
}
