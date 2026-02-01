package com.tayek.util.io;
import java.io.*;
import java.net.URL;
import java.util.Properties;
public class PropertiesIO {
	public static void loadPropertiesFile(Properties properties,String filename) {
		File file=new File(filename);
		try {
			InputStream in=new FileInputStream(file);
			properties.load(in);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void load(Properties properties,String filename,URL url) {
		if(url!=null) try {
			InputStream in=url.openStream();
			if(in!=null) {
				properties.load(in);
			} else System.out.println("properties stream is null for url: "+url);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		else System.out.println("url is null for filename: "+filename);
	}
	public static void writePropertiesFile(Properties properties,String filename) {
		try {
			File file=new File(filename);
			System.out.println("writing new properties to: "+filename+": "+properties);
			properties.store(new FileOutputStream(file),"initial");
		} catch(FileNotFoundException e) {
			System.out.println("properties"+" "+"caught: "+e+" property file was not written!");
		} catch(IOException e) {
			System.out.println("properties"+" "+"caught: "+e+" property file was not written!");
		}
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
		final Properties p=new Properties(defaultProperties);
		try {
			p.load(inputStream);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		return p;
	}
	public static Properties load(final File propertiesFile) {
		Properties p=null;
		try {
			final InputStream in=new FileInputStream(propertiesFile);
			p=load(in);
		} catch(FileNotFoundException e) {
			System.out.println("load caught: "+e);
		}
		return p;
	}
	public static void store(final File propertiesFile,Properties p) {
		try {
			final OutputStream out=new FileOutputStream(propertiesFile);
			store(out,p);
			out.close();
		} catch(FileNotFoundException e) {
			throw new RuntimeException(e);
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
	public static void storeXml(final OutputStream outputStream,final Properties properties) {
		try {
			properties.storeToXML(outputStream,null);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void storeXml(final File propertiesFile,Properties p) {
		try {
			final OutputStream out=new FileOutputStream(propertiesFile);
			storeXml(out,p);
			out.close();
		} catch(FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static final Properties defaultProperties=new Properties();
	static { /* add some properties */ }
}
