/*
 * DEV-J130. Задача №2.
 */

package com.company;

/**
 * Класс представляет общее исключение, возникающее при работе с документами.
 * @author (C)Y.D.Zakovryashin, 12.11.2020
 */
public class DocumentException extends Exception {

    private DocumentException() {}

    public DocumentException(String string) {
        super(string);
    }
    
}
