/*
 * DEV-J130. Задача №2.
 */
package com.company;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * Класс представляет одну запись из таблицы Authors базы данных, т.е. данные об
 * авторах документов. Таблица Authors имеет следующую структуру: -
 * идентификатор автора, представленный целым числом, является первичным ключом
 * таблицы; - поле с именем и фамилией автора длиной до 64 символов
 * включительно, которое не может быть пустым; - поле примечания длиной до 255
 * символов включительно.
 *
 * @author ((C)Y.D.Zakovryashin, 12.11.2020
 */
public class Authors {

    public static final int VERSION = 79897;
    private int author_id;
    private String author;
    private String notes;

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

    public Authors(int author_id, String author) {

        this(author_id, author, null);
    }

    public Authors(int author_id, String author, String notes) {
        this.author_id = author_id;
        this.author = author;
        this.notes = notes;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) { this.author_id = author_id; }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static boolean prepareSQLData(Statement statem) {

        try {

            // Tables create
            statem.execute(TABLE_AUTHORS);

            // Data for table Authors
            for(var i = 0; i < TABLE_AUTHORS_DATA.length; ++i)
                statem.execute(TABLE_AUTHORS_DATA[i]);

            return true;

        } catch(SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int hashCode() {
        return VERSION + this.author_id + Objects.hashCode(this.author);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Authors)) {
            return false;
        }

        final Authors other = (Authors) obj;
        return !(this.author_id != other.author_id
                || !Objects.equals(this.author, other.author));
    }
}
