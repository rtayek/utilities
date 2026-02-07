package com.tayek.util.io;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import com.tayek.util.core.Texts;
public class FileIO {
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
	public static void toFile(final byte[] b,final File file) {
		try {
			OutputStream out=new FileOutputStream(file);
			out.write(b);
			out.close();
		} catch(FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void toFile(final String s,final File file) {
		try {
			Writer out=new FileWriter(file);
			out.write(s);
			out.close();
		} catch(FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void fromReader(final StringBuffer stringBuffer,Reader reader) {
		if(reader!=null) try {
			int c=0;
			while((c=reader.read())!=-1)
				stringBuffer.append((char)c);
			reader.close();
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
	public static void fromFile(final StringBuffer stringBuffer,final File file) {
		Reader r=null;
		try {
			r=new FileReader(file);
			fromReader(stringBuffer,r);
		} catch(FileNotFoundException e) {
			System.out.println(file+" fromFile caught: "+e);
		}
	}
	public static String fromFile(final File file) {
		StringBuffer stringBuffer=new StringBuffer();
		fromFile(stringBuffer,file);
		return stringBuffer.toString();
	}
	public static List<String> toStrings(final BufferedReader r) {
		final List<String> l=new LinkedList<String>();
		String line=null;
		try {
			for(line=r.readLine();(line=r.readLine())!=null;)
				l.add(line);
		} catch(IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return l;
	}
	public static List<String> getFileAsListOfStrings(final File file) {
		BufferedReader r=null;
		try {
			r=new BufferedReader(new FileReader(file));
			final List<String> l=toStrings(r);
			r.close();
			return l;
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static List<String> getDataThatMayHaveLineFeeds(final String[] data) {
		final BufferedReader r=new BufferedReader(new StringReader(Texts.cat(data)));
		return toStrings(r);
	}
	public static Reader toReader(File file) {
		Reader reader=null;
		if(file.exists()&&file.canRead()) {
			try {
				reader=new FileReader(file);
			} catch(IOException e) {
				System.out.println(file+" toReader caught: "+e);
			}
		}
		return reader;
	}
	public static Reader toReader(String string) {
		return new StringReader(string);
	}
	public static BufferedReader toBufferedReader(String string) {
		return new BufferedReader(new StringReader(string));
	}
	public static Writer toWriter(File file) {
		Writer writer=null;
		try {
			writer=new FileWriter(file);
		} catch(IOException e) {
			System.out.println(file+" toWriter caught: "+e);
		}
		return writer;
	}
	public static void close(final Reader r) {
		try {
			r.close();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void close(final Writer w) {
		try {
			w.close();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
