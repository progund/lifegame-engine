package se.juneday.lifegame.util;

import se.juneday.lifegame.engine.LifeGameEngine;

import java.io.PrintStream;

public class Log {

    public enum LogLevel {
      QUIET,
      ERROR,
      WARN,
      INFO,
      DEBUG,
      VERBOSE;

    }

    private static LogLevel logLevel = LogLevel.ERROR;
    private static String includeFilter ;
    private static String includeTag ;
    private static PrintStream out = System.out;

    public static void logLevel(LogLevel logLevel) {
        Log.logLevel = logLevel;
    }

    public static void includeFilter(String includeFilter) {
        Log.includeFilter = includeFilter;
    }

    public static void includeTag(String includeTag) {
        Log.includeTag = includeTag;
    }

    private static void printTagMessage(String tag, String message) {
        if (includeTag!=null) {
          if (!tag.contains(includeTag)) {
                return;
            }
        }
        if (includeFilter!=null) {
            if (!message.contains(includeFilter)) {
                return;
            }
        }
        out.println(tag + ":" + message);
    }

    public static void d(String tag, String message) {
      if (logLevel.compareTo(LogLevel.DEBUG) >= 0) {
        printTagMessage(tag, message);
      }
    }

    public static void e(String tag, String message) {
      if (logLevel.compareTo(LogLevel.ERROR) >= 0) {
        printTagMessage(tag, message);
      }
    }

}
