package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        Authors a1 = new Authors (0, "Authors 1", "Info 1");

        try {


            // Добавили нового автора
            addAuthor(a1);

            // Отредактировали notes для нового автора 1
            a1.setNotes("It is new notes!");

            // Отредактировали notes для нового автора 2
            addAuthor(a1);

            Documents doc1 = new Documents(0, "The new book!", "text-text-text", a1.getAuthor_id());
            Documents doc2 = new Documents(0, "The new 2!", "text", a1.getAuthor_id());

            // Новый документ автора a1 выше
            addDocument(doc1, a1);
            addDocument(doc2, a1);

            // Найдем что там написал это автор
            findDocumentByAuthor(a1);

        } catch (DocumentException err) {

             err.printStackTrace();
        }

/*
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

    @Override
    public boolean addAuthor(Authors author) throws DocumentException {

        int id = author.getAuthor_id();
        String name = author.getAuthor();
        String notice = author.getNotes();

        String sql = null;
        PreparedStatement preparedStatement = null;

        try {

            if(id > 0) {

                sql = "UPDATE `Authors` SET `info` = ? WHERE `id` = ?";

                preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, notice);
                preparedStatement.setInt(2, id);

            } else {

                sql = "INSERT INTO `Authors` (`name`, `info`) VALUES (?, ?)";

                preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, notice);
            }

            preparedStatement.execute();

            int lastInsertedId = -1;
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if(rs.next())
                lastInsertedId = rs.getInt(1);

            author.setAuthor_id( lastInsertedId );

            // false – отредактировали, true – создали новую запись
            return id > 0 ? false : true;

        } catch (SQLException err) {

            throw new DocumentException( err.toString() );
        }
    }

    @Override
    public boolean addDocument(Documents doc, Authors author) throws  DocumentException {

        int idAut = author.getAuthor_id();

        int idDoc = doc.getDocument_id();
        String nameDoc = doc.getTitle();
        String text = doc.getText();

        String sql = null;
        PreparedStatement preparedStatement = null;

        try {

            if(idDoc > 0) {

                sql = "UPDATE `Documents` SET `name` = ?, `info` = ? WHERE `id` = ?";

                preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, nameDoc);
                preparedStatement.setString(2, text);
                preparedStatement.setInt(3, idDoc);

            } else {

                sql = "INSERT INTO `Documents` (`name`, `info`, `author`) VALUES (?, ?, ?)";

                preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, nameDoc);
                preparedStatement.setString(2, text);
                preparedStatement.setInt(3, idAut);
            }

            preparedStatement.execute();

            int lastInsertedId = -1;
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if(rs.next())
                lastInsertedId = rs.getInt(1);

            doc.setAuthor_id( lastInsertedId );

            // false – отредактировали, true – создали новую запись
            return idDoc > 0 ? false : true;

        } catch (SQLException err) {

            throw new DocumentException( err.toString() );
        }
    }

    @Override
    public Documents[] findDocumentByAuthor(Authors author) throws  DocumentException {


        int id = author.getAuthor_id();
        String name = author.getAuthor();

        String sql = "SELECT *, (SELECT COUNT(`id`) FROM Documents WHERE `author` = ?) as size FROM Documents WHERE `author` = ? OR name = ?";

        PreparedStatement preparedStatement = null;

        try {

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            preparedStatement.setString(3, name);

            ResultSet rs = preparedStatement.executeQuery();
            SimpleDateFormat paterStrDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int i = 0;

            Documents[] resObj = null;

            while(rs.next()) {

                if(resObj == null)
                    resObj = new Documents[rs.getInt("size")];

                Date attr4 = new Date(1970, 0, 1);

                try {
                    attr4 = paterStrDate.parse(rs.getString(4));
                } catch (ParseException err){}


                resObj[i++] = new Documents(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        attr4,
                        id
                );

            }

            return resObj;

        } catch (SQLException err) {

            throw new DocumentException( err.toString() );
        }
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

    public static DbServer run() {

        if(DbServer.dbServ != null)
            return DbServer.dbServ;

        DbServer.dbServ = new DbServer();

        return DbServer.dbServ;
    }

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

        //System.out.println("__close");
    }
}
