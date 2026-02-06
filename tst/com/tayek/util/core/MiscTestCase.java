package com.tayek.util.core;
import static org.junit.Assert.*;
import org.junit.Test;
public class MiscTestCase {
    @Test public void testIsValidName() {
        assertTrue(Misc.isValidName("Pair"));
        assertFalse(Misc.isValidName("foo.Pair"));
        assertFalse(Misc.isValidName("Pair<K,V>"));
    }
}
