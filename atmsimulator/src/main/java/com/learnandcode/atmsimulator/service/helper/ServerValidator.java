package com.learnandcode.atmsimulator.service.helper;

import com.learnandcode.atmsimulator.exception.ServerConnectionException;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ServerValidator {
    public void checkServerConnection() {
        if (new Random().nextInt(10) == 0) {
            throw new ServerConnectionException("Unable to connect to server. Please try again.");
        }
    }
}
