package com.tayek.utilities;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.*;
public class Utilities {
	public static String method() {
		return Thread.currentThread().getStackTrace()[2].getClassName()+'.'+Thread.currentThread().getStackTrace()[2].getMethodName()+"()";
	}
	public static String shortMethod() {
		return '.'+Thread.currentThread().getStackTrace()[2].getMethodName()+"()";
	}
	public static Properties load(final Reader reader) {
		try {
			Properties properties=new Properties();
			properties.load(reader);
			return properties;
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static Properties load(final InputStream inputStream) {
		final Properties p=new Properties(defaultProperties); // add in any
																// new defaults?
		try {
			p.load(inputStream);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		return p;
	}
	public static final Properties defaultProperties=new Properties();
	static { /* add some properties */}
	public static String getString(String key,ResourceBundle resourceBundle) {
		String string=null;
		try {
			string=resourceBundle.getString(key);
		} catch(MissingResourceException e) {}
		return string;
	}
	public static void storeXml(final OutputStream outputStream,final Properties properties) {
		try {
			properties.storeToXML(outputStream,null);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void store(final OutputStream outputStream,final Properties properties) {
		try {
			properties.store(outputStream,null);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void store(final Writer writer,final Properties properties) {
		try {
			properties.store(writer,null);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void storeXml(final File propertiesFile,Properties p) {
		try (OutputStream out=Files.newOutputStream(propertiesFile.toPath())) {
			storeXml(out,p);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static Properties load(final File propertiesFile) {
		try (InputStream in=Files.newInputStream(propertiesFile.toPath())) {
			return load(in);
		} catch(NoSuchFileException|AccessDeniedException e) {
			System.out.println(e);
			return null;
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void store(final File propertiesFile,Properties p) {
		try (OutputStream out=Files.newOutputStream(propertiesFile.toPath())) {
			store(out,p);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static byte[] save(final Object o) {
		try {
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			try (ObjectOutputStream out=new ObjectOutputStream(baos)) {
				out.writeObject(o);
				out.flush();
			}
			return baos.toByteArray();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static Object restore(final ObjectInputStream objectInputStream) {
		try {
			final Object o=objectInputStream.readObject();
			objectInputStream.close();
			return o;
		} catch(IOException e) {
			throw new RuntimeException(e);
		} catch(ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public static Object restore(final byte[] b) {
		try {
			ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(b));
			return restore(in);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static Object restore(final File file) {
		try (ObjectInputStream in=new ObjectInputStream(Files.newInputStream(file.toPath()))) {
			return restore(in);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void toFile(final byte[] b,final File file) {
		try {
			Files.write(file.toPath(),b);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void toFile(final String s,final File file) {
		try (Writer out=Files.newBufferedWriter(file.toPath(),Charset.defaultCharset())) {
			out.write(s);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void fromFile(final StringBuffer stringBuffer,final File file) {
		try (Reader r=Files.newBufferedReader(file.toPath(),Charset.defaultCharset())) {
			int c=0;
			while ((c=r.read())!=-1)
				stringBuffer.append((char)c);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static List<String> toStrings(final BufferedReader r) {
		final List<String> l=new LinkedList<String>();
		try {
			for(String line=r.readLine();line!=null;line=r.readLine())
				l.add(line);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		return l;
	}
	static String cat(final String[] data) {
		final StringBuffer sb=new StringBuffer();
		for(int i=0;i<data.length;i++)
			sb.append(data[i]).append('\n');
		return sb.toString();
	}
	public static List<String> getFileAsListOfStrings(final File file) {
		try {
			return Files.readAllLines(file.toPath(),Charset.defaultCharset());
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	static List<String> getDataThatMayHaveLineFeeds(final String[] data) {
		final BufferedReader r=new BufferedReader(new StringReader(cat(data))); // mes1
																				// has
																				// line
																				// feeds!
		return toStrings(r);
	}
	public static void removeCr(final StringBuffer stringBuffer,final String string) {
		for(int i=0;i<string.length();i++)
			if(string.charAt(i)!='\r') stringBuffer.append(string.charAt(i));
	}
	public static String noEol(String string) {
		String s=string;
		if(s.charAt(s.length()-1)=='\n') s=s.substring(0,s.length()-1);
		if(s.charAt(s.length()-1)=='\r') s=s.substring(0,s.length()-1);
		return s;
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
	public static int uniform(final int n) {
		return (int)Math.floor(Math.random()*n);
	}
	public static String runCommand(String classToRun) {
		String cp="-cp bin";
		File lib=new File("lib");
		FilenameFilter jarFilter=new FilenameFilter() {
			@Override public boolean accept(File arg0,String arg1) {
				return arg1.endsWith(".jar");
			}
		};
		if(lib.exists()&&lib.isDirectory()) for(String name:lib.list(jarFilter))
			cp+=";"+"lib/"+name;
		String command="java "+cp+" "+classToRun;
		return command;
	}
	public static void createBatchFile(String filename,String classToRun) {
		String command=runCommand(classToRun);
		File file=new File(filename);
		toFile(command,file);
	}
}
