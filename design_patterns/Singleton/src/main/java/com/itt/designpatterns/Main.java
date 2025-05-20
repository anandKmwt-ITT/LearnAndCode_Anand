package com.itt.designpatterns;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getInstance();
        logger.logDebug("Application Started!");
        logger.logError("Something went wrong!");
    }
}