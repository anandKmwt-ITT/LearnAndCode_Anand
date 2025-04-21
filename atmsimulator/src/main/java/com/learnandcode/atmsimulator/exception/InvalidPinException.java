package com.learnandcode.atmsimulator.exception;

public class InvalidPinException extends RuntimeException {
    public InvalidPinException(String message) {
        super(message);
    }
}
