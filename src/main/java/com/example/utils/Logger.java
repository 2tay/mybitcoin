package com.example.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String DIRECTORY_NAME = "Logs";
    private static final String FILE_NAME = Logger.class.getSimpleName() + ".txt";
    private static BufferedWriter writer;

    static {
        try {
            File directory = new File(DIRECTORY_NAME);
            if (!directory.exists()) {
                directory.mkdir();
            }
            writer = new BufferedWriter(new FileWriter(new File(directory, FILE_NAME), true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        try {
            if (writer != null) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                writer.write(timestamp + " - " + message);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Logger.log("This is a test message.");
        Logger.log("Another message.");
        Logger.close();
    }
}
