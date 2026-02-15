package com.tayek.util.misc;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
public class Tee extends FilterOutputStream {
	// make this into a writer or make a version for writers
	// not a good idea unless we use the apache thing
	public Tee(OutputStream out) {
		super(out);
		stream.addElement(out);
		printStream=new PrintStream(this,true) {
			@Override public void println(String string) { super.println(prefix+string); }
		};
	}
	public void teeTo() {
		// Tee tee=new Tee(fileOutputStream);
		// tee.addOutputStream(System.out); /* make into a constructor and put
		// into Tee? */
		// seems like we don't have a need for this yet
	}
	public synchronized void addOutputStream(OutputStream out) {
		stream.addElement(out);
	}
	protected final Vector<OutputStream> outputs() {
		return stream;
	}
	@Override
	public synchronized void write(int b) throws IOException {
		for(Enumeration<OutputStream> e=stream.elements();e.hasMoreElements();) {
			OutputStream out=e.nextElement();
			out.write(b);
			out.flush();
		}
	}
	@Override
	public synchronized void write(byte[] data,int offset,int length) throws IOException {
		Integer i=0;
		for(Enumeration<OutputStream> e=stream.elements();e.hasMoreElements();) {
			OutputStream out=e.nextElement();
			if(verbose) {
				String index=i.toString();
				out.write(index.charAt(0)); // fails if more than10 streams.
				out.write('>');
			}
			out.write(data,offset,length);
			out.flush();
			++i;
		}
	}
	public static Tee tee(File file) {
		return tee(file,false);
	}
	public static Tee teeDeleting(File file) {
		return tee(file,true);
	}
	public static Tee tee(File file,boolean delete) {
		if(delete&&file.exists()&&!file.delete()) System.out.println(file+" was not deleted!");
		try {
			OutputStream outputStream=Files.newOutputStream(file.toPath(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
			Tee tee=new Tee(outputStream);
			tee.addOutputStream(System.out);
			tee.addOutputStream(System.err);
			tee.setOut();
			tee.setErr();
			return tee;
		} catch(IOException e) {
			System.out.println(e);
			System.out.println("tee failed!");
			return null;
		}
	}
	public PrintStream setOut() { previousOut=System.out; System.setOut(printStream); return previousOut; }
	public PrintStream restoreOut() {
		PrintStream previous=System.out;
		if(previousOut!=null) System.setOut(previousOut);
		return previous;
	}
	public PrintStream setErr() { previousErr=System.err; System.setErr(printStream); return previousErr; }
	public PrintStream restoreErr() {
		PrintStream previous=System.err;
		if(previousErr!=null) System.setErr(previousErr);
		return previous;
	}
	public void restoreBoth() { if(previousOut!=null) restoreOut(); if(previousErr!=null) restoreErr(); }
	public static void printStuff(PrintStream printStream,PrintStream sysout,PrintStream syserr) {
		sysout.println("1 sysout "); // just comes out on sysout
		syserr.println("2 syserr"); // ditto
		printStream.println("ps from tee");
		sysout.flush();
		syserr.flush();
		printStream.flush();
	}
	public boolean verbose;
	public String prefix="T ";
	protected PrintStream previousOut,previousErr;
	public final PrintStream printStream;
	private final Vector<OutputStream> stream=new Vector<OutputStream>();
}
