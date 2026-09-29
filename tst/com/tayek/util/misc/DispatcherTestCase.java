package com.tayek.util.misc;
import static org.junit.Assert.*;
import org.junit.Test;
public class DispatcherTestCase {
    @Test public void testUnknownNumberIsRejectedNotThrown() {
        Dispatcher dispatcher=new Dispatcher(new String[0]);
        assertNull(dispatcher.run(99,"99",new String[] {"99"}));
    }
}
