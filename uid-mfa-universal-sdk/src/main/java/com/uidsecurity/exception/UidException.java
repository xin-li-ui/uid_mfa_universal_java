package com.uidsecurity.exception;

public class UidException extends RuntimeException {

    public UidException(String message) {
        super(message);
    }

    public UidException(Throwable e) {
        super(e);
    }

    public UidException(String message, Throwable e) {
        super(message, e);
    }
}
