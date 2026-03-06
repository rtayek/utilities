package com.tayek.util.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class AnotherFormatter extends Formatter {
    @Override public String format(LogRecord record) {
        String threadName = Thread.currentThread().getName();
        String className = record.getSourceClassName();
        int x = className.lastIndexOf(".");
        className = className.substring(x + 1);
        long time = System.currentTimeMillis();
        long dt = (time - t0) % 1_000_000;
        if(threadName.length() > maxThreadNameLength) threadName = threadName.substring(0, maxThreadNameLength - 2) + threadName.substring(threadName.length() - 1);
        return String.format(format, dt, record.getSequenceNumber(), record.getLevel(), record.getMessage(), threadName,
                className + "." + record.getSourceMethodName() + "()");
    }

    public static long t0 = 0;
    public static final Integer maxThreadNameLength = 10;
    public static final String format = "%06d %05d %7s %-45s in %" + maxThreadNameLength + "s %s\n";
}
