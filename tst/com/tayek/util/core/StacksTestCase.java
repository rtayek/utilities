package com.tayek.util.core;
import java.io.*;
import com.tayek.util.io.FileIO;
import junit.framework.*;
public class StacksTestCase extends TestCase {
	public StacksTestCase(java.lang.String testName) {
		super(testName);
	}
	public static Test suite() {
		TestSuite suite = new TestSuite(StacksTestCase.class);
		return suite;
	}
	public void testP() {
		final File file=new File("foo/bar");
		FileIO.p(file);
	}
	public static void main(String[] argument) throws Exception {
		junit.textui.TestRunner.run(suite());
	}
}
