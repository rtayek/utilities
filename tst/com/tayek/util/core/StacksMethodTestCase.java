package com.tayek.util.core;
import static org.junit.Assert.*;
import org.junit.Test;
public class StacksMethodTestCase {
    @Test public void testMethodNamesTheCaller() {
        assertEquals(StacksMethodTestCase.class.getName()+".testMethodNamesTheCaller()",Stacks.method());
        assertEquals(".testMethodNamesTheCaller()",Stacks.shortMethod());
    }
    @Test public void testMethod2IsTheCallerOfMethodInt() {
        assertEquals(StacksMethodTestCase.class.getName()+".testMethod2IsTheCallerOfMethodInt()",Stacks.method(2));
    }
    String callee() { return Stacks.method(3); }
    @Test public void testMethod3IsTheCallersCaller() { // how rtgo's Coordinates uses it
        assertEquals(StacksMethodTestCase.class.getName()+".testMethod3IsTheCallersCaller()",callee());
    }
}
