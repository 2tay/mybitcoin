package com.example.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Logger {
    public static int lineNumber = 0;
    public static Map<String, Logger> LoggersMap = Collections.synchronizedMap(new HashMap<>());
    public BufferedWriter loggerWriter;

    public static Logger getLogger(Object o) {
        String className = o.getClass().getSimpleName();
        if (LoggersMap.containsKey(className)) {
            return LoggersMap.get(className);
        } else {
            Logger newLogger = new Logger(className);
            LoggersMap.put(className, newLogger);
            return newLogger;
        }
    }

    public static void writeLogs(Object o, String txt) {
        try {
            txt = lineNumber + "'s : " + o.toString().hashCode() + " : " + txt;
            Logger lg = Logger.getLogger(o);
            lg.loggerWriter.write(txt);
            lg.loggerWriter.newLine();
            lg.loggerWriter.flush();
            lineNumber += 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Logger(String className) {
        try {
            File dir = new File("Logs");
            if (!dir.exists()) {
                dir.mkdir();
            }
            String path = "Logs/" + className + ".txt";
            this.loggerWriter = new BufferedWriter(new FileWriter(path, true)); // Append mode
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.loggerWriter != null) {
            this.loggerWriter.close();
        }
    }

    public static void main(String[] args) {
        class Student {
            String name;
            Student(String name) {
                this.name = name;
            }
        }
        class School {
            String name;
            School(String name) {
                this.name = name;
            }
        }

        Student s1 = new Student("abdelhadi");
        Student s2 = new Student("Mstafa");
        School sc1 = new School("xxx");
        School sc2 = new School("yyy");

        Logger.writeLogs(s1, "abdelhadi is writing...");
        Logger.writeLogs(s2, "mstafa is writing...");
        Logger.writeLogs(sc1, "xxx is writing...");
        Logger.writeLogs(sc2, "yyy is writing...");
        Logger.writeLogs(s1, "abdelhadi 2 is writing...");
        Logger.writeLogs(s2, "mstafa 2 is writing...");
    }
}
