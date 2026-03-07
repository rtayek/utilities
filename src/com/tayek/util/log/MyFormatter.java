package com.tayek.util.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {
    public MyFormatter() {
        this(true);
    }

    public MyFormatter(boolean useColor) {
        this(useColor, DEFAULT_MAX_THREAD_NAME_LENGTH);
    }

    public MyFormatter(boolean useColor, int maxThreadNameLength) {
        this.useColor = useColor;
        this.maxThreadNameLength = maxThreadNameLength;
        format = "%05d %" + maxThreadNameLength + "s %7s %32s in %s\n";
    }

    @Override public String format(LogRecord record) {
        String name = Thread.currentThread().getName();
        if(name.length() == 0) System.out.println("thread name is empty!");
        if(name.length() > maxThreadNameLength) name = name.substring(0, maxThreadNameLength - 3) + '~' + name.substring(name.length() - 1);
        String line = String.format(format, record.getSequenceNumber(), name, record.getLevel(), record.getMessage(),
                record.getSourceClassName() + "." + record.getSourceMethodName() + "()");
        String coloredLine = useSequence ? Sequence.color(line, name) : ConsoleAndLogColors.color(line);
        return useColor ? coloredLine : line;
    }

    public boolean useSequence = true;
    public static final int DEFAULT_MAX_THREAD_NAME_LENGTH = 12;
    public final String format;
    private final boolean useColor;
    private final int maxThreadNameLength;
}
