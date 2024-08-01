package com.example.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OutputWriter {
    private static PrintWriter writer;

    static {
        try {
            // Initialize the PrintWriter to write to output.txt
            writer = new PrintWriter(new FileWriter("output.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); // Exit the program if there's an error initializing the writer
        }
    }

    public static void writeOutput(String message) {
        writer.println(message);
        writer.flush(); // Ensure the message is written to the file
    }

    public static void closeWriter() {
        if (writer != null) {
            writer.close();
        }
    }

    public static void main(String[] args) {
        // Test writing to the file
        writeOutput("Hello, World!");
        writeOutput("This is a test message.");
        
        // Close the writer when done
        closeWriter();
    }
}
