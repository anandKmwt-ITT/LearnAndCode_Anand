package com.itt.newsAggrigationClient.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ConsoleUtils {

    public static void printGreeting(String username) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("h:mma"));

        System.out.println("\nWelcome to the News Application, " + username + "! Date: " + date);
        System.out.println("Time: " + time);
        System.out.println("\nPlease choose the options below");
    }
}
