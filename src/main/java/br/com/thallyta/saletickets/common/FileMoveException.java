package br.com.thallyta.saletickets.common;

public class FileMoveException extends RuntimeException {
    public FileMoveException(String message) {
        super(message);
    }

    public FileMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}

