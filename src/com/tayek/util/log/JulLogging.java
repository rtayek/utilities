package com.tayek.util.log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Reusable JUL handler wiring utilities shared by multiple projects.
 */
public final class JulLogging {
    private JulLogging() {}

    public static StreamHandler flushingStreamHandler(OutputStream outputStream) {
        return flushingStreamHandler(outputStream,new SimpleFormatter(),Level.ALL);
    }

    public static StreamHandler flushingStreamHandler(OutputStream outputStream,Formatter formatter,Level level) {
        StreamHandler streamHandler=new StreamHandler(outputStream,formatter) {
            @Override public synchronized void publish(final LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        streamHandler.setLevel(level);
        return streamHandler;
    }

    public static void clearHandlers(Logger logger) {
        if(logger==null) return;
        Handler[] handlers=logger.getHandlers();
        for(Handler handler:handlers) {
            logger.removeHandler(handler);
            try {
                handler.close();
            } catch(Exception ignored) {}
        }
    }

    public static Handler addConsoleHandler(Logger logger,Formatter formatter,Level level) {
        Handler consoleHandler=new ConsoleHandler();
        consoleHandler.setLevel(level);
        if(formatter!=null) consoleHandler.setFormatter(formatter);
        logger.addHandler(consoleHandler);
        return consoleHandler;
    }

    public static Handler addFileHandler(Logger logger,String pattern,int limit,int count,boolean append,Formatter formatter,Level level)
            throws SecurityException,IOException {
        Handler fileHandler=new FileHandler(pattern,limit,count,append);
        fileHandler.setLevel(level);
        if(formatter!=null) fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);
        return fileHandler;
    }
}
