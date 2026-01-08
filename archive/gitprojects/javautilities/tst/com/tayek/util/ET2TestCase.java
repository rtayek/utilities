package com.tayek.util;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ET2TestCase {
	@Before public void setUp() throws Exception {}
	@After public void tearDown() throws Exception {}
	@Test public void testET() {
		et=new ET2();
	}
	@Test public void testETLong() {
		long t0=System.currentTimeMillis();
		et=new ET2(t0);
		assertEquals(t0,et.t0);
	}
	@Test public void testEt() {
		et=new ET2();
		long t=et.et();
		assertEquals(0,t);
	}
	@Test public void testEtLong() {
		et=new ET2();
		long dt=et.et(et.t0);
		assertEquals(0,dt);
	}
	@Test public void testRateLong() {
		long t0=System.currentTimeMillis();
		et=new ET2(t0);
		double rate=et.rate(1);
		System.out.println(rate);
		assertEquals(Double.POSITIVE_INFINITY,rate,0);
	}
	@Test public void testRateLongLong() {
		long t0=System.currentTimeMillis();
		et=new ET2(t0);
		double rate=et.rate(1,t0);
		assertEquals(Double.POSITIVE_INFINITY,rate,0);
	}
	@Test public void testRateLongLong2() {
		long t0=System.currentTimeMillis();
		et=new ET2(t0);
		double rate=et.rate(1,t0+1);
		assertEquals(1000,rate,0);
	}
	ET2 et;
}
