package com.tck.capBackend.Exception;

public class PasswordBlankException extends Throwable {
    public PasswordBlankException() {
        super("Password cannot be blank.");
    }
}
