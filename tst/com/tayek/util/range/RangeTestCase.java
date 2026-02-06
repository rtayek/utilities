package com.tayek.util.range;
import static org.junit.Assert.*;
import org.junit.Test;
import static com.tayek.util.range.Range.*;
public class RangeTestCase {
    @Test public void testContainsUpperBound() {
        assertTrue(range(1,2).contains(2));
    }
}
