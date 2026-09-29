package com.tayek.util.range;
import static org.junit.Assert.*;
import org.junit.Test;
public class MissingRangesTestCase {
    Missing<Integer,Range<Integer>> m=Missing.factory.createRanges(0);
    @Test public void testAdjustDoesNotMarkNItselfMissing() {
        m.adjust(3);
        assertEquals(Integer.valueOf(3),m.largest());
        assertEquals(3,m.missing().size()); // 0,1,2
        assertFalse(m.isMissing(3));
    }
    @Test public void testInOrderLeavesNothingMissing() {
        for(int i=0;i<5;i++) m.adjust(i);
        assertFalse(m.areAnyMissing());
        assertFalse(m.areAnyOutOfOrder());
    }
    @Test public void testOutOfOrderIsReducedToOneRange() {
        m.adjust(4); // 0..3 missing
        m.adjust(1);
        m.adjust(2);
        assertEquals(2,m.missing().size()); // 0 and 3
        assertEquals(1,m.outOfOrder().size());
        assertEquals(Range.range(1,2),m.outOfOrder().iterator().next());
    }
    @Test public void testRepeatedOutOfOrderIsNotAddedTwice() {
        m.adjust(4);
        m.adjust(1);
        m.adjust(1); // duplicate: already in an out of order range
        assertEquals(1,m.outOfOrder().size());
    }
}
