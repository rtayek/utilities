package com.tayek.util.log;

import static org.junit.Assert.assertEquals;
import java.util.logging.Logger;
import org.junit.Test;

public class ColorLogsTestCase {
    private static final Logger logger=Logger.getLogger(ColorLogsTestCase.class.getName());
    @Test public void test() {
        for(String key:ConsoleAndLogColors.map.keySet()) {
            String expected=ConsoleAndLogColors.map.get(key);
            String actual=ConsoleAndLogColors.escapeSequence(key);
            assertEquals(expected,actual);
            actual=ConsoleAndLogColors.escapeSequence("foo"+key+"bar");
            assertEquals(expected,actual);
        }
    }
    @Test public void testLog() {
        for(String key:ConsoleAndLogColors.map.keySet()) {
            logger.info(String.valueOf(ConsoleAndLogColors.color(key)));
            logger.severe(ConsoleAndLogColors.color(key));
        }
    }
}
