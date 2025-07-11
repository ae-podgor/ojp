package ru.otus.homework.dataprocessor;

public class FileProcessException extends RuntimeException {
    public FileProcessException(Exception ex) {
        super(ex);
    }

    public FileProcessException(String msg) {
        super(msg);
    }

    public FileProcessException(String msg, Exception ex) {
        super(msg, ex);
    }
}
