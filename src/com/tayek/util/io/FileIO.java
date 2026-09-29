package com.tayek.util.io;
import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.tayek.util.core.Texts;
/**
 * File and reader helpers. Text is read and written as UTF-8, the default charset since Java 18.
 * Methods that return null or print on failure keep doing so; the ...OrThrow variants throw.
 */
public class FileIO {
	/** Adds dir, or every file under it, to files (a new list if files is null). */
	public static List<File> addFiles(List<File> files,File dir) {
		if(files==null) files=new LinkedList<File>();
		if(!dir.isDirectory()) {
			files.add(dir);
			return files;
		}
		for(File file:dir.listFiles())
			addFiles(files,file);
		return files;
	}
	/** Prints the various names Java gives file. */
	public static void p(File file) {
		try {
			System.out.println("file="+file);
			System.out.println("file "+file+" "+(file.exists()?"exists":"does not exist"));
			System.out.println(".toString()="+file.toString());
			System.out.println("parent="+file.getParent()+",separator="+File.separator+", name="+file.getName());
			System.out.println(".getPath()="+file.getPath());
			System.out.println(".getAbsolutePath()="+file.getAbsolutePath());
			System.out.println(".getCanonicalPath()="+file.getCanonicalPath());
			System.out.println(".getCanonicalFile()="+file.getCanonicalFile());
			System.out.println(".getName()="+file.getName());
		} catch(IOException e) {
			System.out.println(e);
		}
	}
	/** Appends everything reader has to stringBuffer, then closes reader. Prints on failure. */
	public static void fromReader(final StringBuffer stringBuffer,Reader reader) {
		if(reader!=null) try(reader) {
			StringWriter writer=new StringWriter();
			reader.transferTo(writer);
			stringBuffer.append(writer);
		} catch(IOException e) {
			System.out.println("fromReader caught: "+e);
			e.printStackTrace();
		}
	}
	public static String fromReader(final Reader reader) {
		StringBuffer stringBuffer=new StringBuffer();
		fromReader(stringBuffer,reader);
		return stringBuffer.toString();
	}
	/** Everything reader has, or null if reader is null. Closes reader. */
	public static String toString(final Reader reader) throws IOException {
		if(reader==null) return null;
		try(reader) {
			StringWriter writer=new StringWriter();
			reader.transferTo(writer);
			return writer.toString();
		}
	}
	/** The whole file, or null if file is null. */
	public static String toString(final File file) throws IOException {
		return file!=null?Files.readString(file.toPath()):null;
	}
	/** The whole file, or "" after printing the error. */
	public static String fromFile(final File file) {
		try {
			return Files.readString(file.toPath());
		} catch(IOException e) {
			System.out.println(file+" fromFile caught: "+e);
			return "";
		}
	}
	public static List<String> toStrings(final BufferedReader r) {
		try {
			return toStrings((Reader)r);
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	/** All lines of reader (empty if reader is null). Closes reader. */
	public static List<String> toStrings(final Reader reader) throws IOException {
		if(reader==null) return Collections.emptyList();
		try(BufferedReader bufferedReader=reader instanceof BufferedReader b?b:new BufferedReader(reader)) {
			return new LinkedList<>(bufferedReader.lines().toList());
		} catch(UncheckedIOException e) {
			throw e.getCause();
		}
	}
	/** All lines of file (empty if file is null). */
	public static List<String> toStrings(final File file) throws IOException {
		return file!=null?Files.readAllLines(file.toPath()):Collections.emptyList();
	}
	/** Writes string to file, replacing it. */
	public static void write(final String string,final File file) {
		try {
			Files.writeString(file.toPath(),string);
		} catch(IOException e) {
			throw new UncheckedIOException("can not write file: "+file,e);
		}
	}
	/** A reader on file, or null if it can not be read. */
	public static Reader toReader(File file) {
		if(!(file.exists()&&file.canRead())) return null;
		try {
			return Files.newBufferedReader(file.toPath());
		} catch(IOException e) {
			System.out.println(file+" toReader caught: "+e);
			return null;
		}
	}
	public static Reader toReaderOrThrow(File file) throws IOException {
		if(!(file.exists()&&file.canRead())) throw new RuntimeException("file not found or can not be read.");
		return Files.newBufferedReader(file.toPath());
	}
	public static Reader toReader(String string) {
		return string!=null?new StringReader(string):null;
	}
	/** A reader on the strings joined with no separator, or null if there are none. */
	public static Reader toReader(String[] strings) {
		String value=Texts.toString(strings);
		return value!=null?new StringReader(value):null;
	}
	/** A writer that replaces file, or null after printing the error. */
	public static Writer toWriter(File file) {
		try {
			return Files.newBufferedWriter(file.toPath());
		} catch(IOException e) {
			System.out.println(file+" toWriter caught: "+e);
			return null;
		}
	}
}
