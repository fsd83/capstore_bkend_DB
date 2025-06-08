package com.tck.capBackend.Exception;

public class UnspecifiedException extends RuntimeException{

    public UnspecifiedException() {
        super("An unspecified error has occurred. Please try again later.");
    }
}
