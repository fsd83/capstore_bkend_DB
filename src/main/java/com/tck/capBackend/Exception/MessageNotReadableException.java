package com.tck.capBackend.Exception;

public class MessageNotReadableException extends RuntimeException{

    public MessageNotReadableException() {
        super("Invalid data. Please check again.");
    }
}
