package com.tayek.util.io;
import java.io.*;
import java.net.URL;
import java.util.Collections;
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
		write(s,file);
	}
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
	public static String toString(final Reader reader) throws IOException {
		if(reader==null) return null;
		try {
			StringBuilder sb=new StringBuilder();
			for(int c=reader.read();c!=-1;c=reader.read())
				sb.append((char)c);
			return sb.toString();
		} finally {
			reader.close();
		}
	}
	public static String toString(final File file) throws FileNotFoundException,IOException {
		return file!=null?toString(new BufferedReader(new FileReader(file))):null;
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
		try {
			return toStrings((Reader)r);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static List<String> toStrings(final Reader reader) throws IOException {
		if(reader==null) return Collections.emptyList();
		final BufferedReader bufferedReader=reader instanceof BufferedReader?(BufferedReader)reader:new BufferedReader(reader);
		try {
			final List<String> lines=new LinkedList<String>();
			for(String line=bufferedReader.readLine();line!=null;line=bufferedReader.readLine())
				lines.add(line);
			return lines;
		} finally {
			bufferedReader.close();
		}
	}
	public static List<String> toStrings(final File file) throws IOException {
		return file!=null?toStrings(new FileReader(file)):Collections.emptyList();
	}
	public static List<String> getListOfLines(BufferedReader bufferedReader) {
		try {
			return toStrings(bufferedReader);
		} catch(RuntimeException e) {
			System.out.println(e);
			return Collections.emptyList();
		}
	}
	public static String get(BufferedReader bufferedReader) {
		StringBuffer stringBuffer=new StringBuffer();
		try {
			for(String line=bufferedReader.readLine();line!=null;line=bufferedReader.readLine())
				stringBuffer.append(line).append(' ');
		} catch(IOException e) {
			System.out.println(e);
		}
		return stringBuffer.toString();
	}
	public static List<String> getAsListOfStrings(URL url) { // get url as list of strings
		List<String> list=null;
		BufferedReader bufferedReader=null;
		InputStream inputStream=null;
		try {
			inputStream=url.openStream();
		} catch(IOException e) {
			e.printStackTrace();
			return list;
		}
		try {
			bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
			list=getListOfLines(bufferedReader);
			bufferedReader.close();
			return list;
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(bufferedReader!=null)
				try {
					bufferedReader.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
		}
		return Collections.emptyList();
	}
	public static String get(URL url) {
		String string=null;
		BufferedReader bufferedReader=null;
		InputStream inputStream=null;
		try {
			inputStream=url.openStream();
		} catch(IOException e) {
			System.out.println(e);
			return string;
		}
		try {
			bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
			string=get(bufferedReader);
			bufferedReader.close();
		} catch(IOException e) {
			System.out.println(e);
		} finally {
			if(bufferedReader!=null)
				try {
					bufferedReader.close();
				} catch(IOException e) {
					System.out.println(e);
				}
		}
		// may return an partial result!
		return string;
	}
	public static String get(final File file) {
		if(!file.canRead()) {
			System.out.println("can not read file: ="+file);
			return null;
		}
		BufferedReader bufferedReader=null;
		String string=null;
		try {
			bufferedReader=new BufferedReader(new FileReader(file));
			string=get(bufferedReader);
		} catch(IOException e) {
			System.out.println(e);
		} finally {
			if(bufferedReader!=null)
				try {
					bufferedReader.close();
				} catch(Exception e) {
					System.out.println(e);
				}
		}
		return string.toString();
	}
	public static void write(final String string,final File file) {
		try(BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file))) {
			bufferedWriter.write(string);
		} catch(Throwable t) {
			t.printStackTrace();
			throw new RuntimeException("can not write file: "+file);
		}
	}
	public static List<String> getFileAsListOfStrings(final File file) {
		try {
			return toStrings(file);
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
