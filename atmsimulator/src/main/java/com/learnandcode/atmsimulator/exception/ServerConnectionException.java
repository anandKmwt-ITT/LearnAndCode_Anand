package com.learnandcode.atmsimulator.exception;

public class ServerConnectionException extends RuntimeException {
    public ServerConnectionException(String message) {
        super(message);
    }
}
