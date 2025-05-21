package com.itt.designpatterns;

public class Logger {

    private static volatile Logger logger;

    private Logger(){
        System.out.println("Logger Initialized!");
    }

    public static Logger getInstance(){
        if(logger == null) {
            synchronized (Logger.class) {
                if(logger == null) {
                    logger = new Logger();
                }
            }
        }
        return logger;
    }

    public void logInfo(String message) {
        System.out.println("[INFO]: " + message);
    }

    public void logDebug(String message) {
        System.out.println("[DEBUG]: " + message);
    }

    public void logError(String message) {
        System.err.println("[ERROR]: " + message);
    }
}
