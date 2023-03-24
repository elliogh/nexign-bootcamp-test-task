package ru.tselikov.util;

/**
 * Класс Логгер
 */
public class Logger {
    private final static String ERROR_COLOR = "\u001B[31m";
    private final static String INFO_COLOR = "\u001B[36m";
    private final static String RESET_COLOR = "\u001B[0m";
    private final static String DEBUG_COLOR = "\u001B[33m";
    private static boolean enableDebug = false;

    public enum LogLevel {
        INFO, ERROR, DEBUG
    }

    public static void info(String message) {
        log(INFO_COLOR ,LogLevel.INFO, message);
    }

    public static void error(String message) {
        log(ERROR_COLOR ,LogLevel.ERROR, message);
    }

    public static void debug(String message) {
        if (enableDebug) {
            log(DEBUG_COLOR, LogLevel.DEBUG, message);
        }
    }

    private static void log(String color,LogLevel level, String message) {
        String logMessage = color + level.name() + RESET_COLOR + ": " + message;
        System.out.println(logMessage);
    }

    public static void enableDebug(boolean enableDebug) {
        Logger.enableDebug = enableDebug;
    }
}

