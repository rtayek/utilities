package com.tayek.util.core;
import static org.junit.Assert.*;
import org.junit.Test;
public class HistogramMinMaxTestCase {
    @Test public void testMaxOfAllNegativeData() {
        Histogram histogram=new Histogram(10,-10,0);
        histogram.add(new double[] {-5,-3,-7});
        assertEquals(-3,histogram.max(),0);
        assertEquals(-7,histogram.min(),0);
    }
    @Test public void testMinAndMaxAfterClear() {
        Histogram histogram=new Histogram(10,-10,0);
        histogram.add(-5);
        histogram.clear();
        histogram.add(-2);
        assertEquals(-2,histogram.max(),0);
        assertEquals(-2,histogram.min(),0);
    }
}
