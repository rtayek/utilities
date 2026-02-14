package com.tayek.util.core;
import java.io.*;
import java.util.*;
import com.tayek.util.io.FileIO;
import static com.tayek.util.io.FileIO.*;
import junit.framework.*;
public class StringUtilitiesTestCase extends TestCase {
	public StringUtilitiesTestCase(java.lang.String testName) {
		super(testName);
	}
	public static Test suite() {
		TestSuite suite=new TestSuite(StringUtilitiesTestCase.class);
		return suite;
	}
	public void testToStringOfArrayOfStringWithNullArray() {
		assertNull(StringUtilities.toString((String[])null));
	}
	public void testToStringOfArrayOfStringWithEmptyArray() {
		assertNull(StringUtilities.toString(new String[0]));
	}
	public void testToStringOfArrayOfStringWithNullIterator() {
		assertNull(StringUtilities.toString((Iterator<String>)null));
	}
	public void testToStringOfArrayOfStringWithEmptyIterator() {
		assertNull(StringUtilities.toString(Collections.emptyListIterator()));
	}
	public void testToStringOfReaderWithNull() throws Exception {
		assertNull(FileIO.toString((Reader)null));
	}
	public void testToStringOfFileWithNull() throws Exception {
		assertNull(FileIO.toString((File)null));
	}
	public void testToReaderWithString() throws Exception {
		final String expected="a";
		assertEquals(expected,FileIO.toString(StringUtilities.toReader(expected)));
	}
	public void testToReaderWithArrayOfString() throws Exception {
		final String[] s=new String[]{"line 1","line 2"};
		final String expected=s[0]+s[1];
		assertEquals(expected,FileIO.toString(StringUtilities.toReader(s)));
	}
	public void testToStringsReadsAllLines() throws Exception {
		final String data="line1\nline2\nline3\n";
		List<String> lines=FileIO.toStrings(new BufferedReader(new StringReader(data)));
		assertEquals(Arrays.asList("line1","line2","line3"),lines);
	}
	public static void main(String[] argument) throws Exception {
		junit.textui.TestRunner.run(suite());
	}
}
