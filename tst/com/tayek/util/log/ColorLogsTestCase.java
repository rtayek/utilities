package com.tayek.util.log;

import static org.junit.Assert.assertEquals;
import java.util.logging.Logger;
import org.junit.Test;

public class ColorLogsTestCase {
    private static final Logger logger=Logger.getLogger(ColorLogsTestCase.class.getName());
    @Test public void test() {
        for(String key:ColorLogs.map.keySet()) {
            String expected=ColorLogs.map.get(key);
            String actual=ColorLogs.escapeSequence(key);
            assertEquals(expected,actual);
            actual=ColorLogs.escapeSequence("foo"+key+"bar");
            assertEquals(expected,actual);
        }
    }
    @Test public void testLog() {
        for(String key:ColorLogs.map.keySet()) {
            logger.info(String.valueOf(ColorLogs.color(key)));
            logger.severe(ColorLogs.color(key));
        }
    }
}
