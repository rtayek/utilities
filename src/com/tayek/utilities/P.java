package com.tayek.utilities;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
public class P {
	private static boolean loadInputStream(Properties properties,InputStream in) {
		boolean rc=false;
		if(in!=null) {
			try {
				properties.load(in);
				rc=true;
			} catch(IOException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					in.close();
				} catch(IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return rc;
	}
	public static void loadProperties(Properties properties,File propertiesFile) {
		try {
			final InputStream is=new FileInputStream(propertiesFile);
			properties.load(is);
			is.close();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static Properties loadProperties(File propertiesFile) {
		final Properties p=new Properties();
		loadProperties(p,propertiesFile);
		return p;
	}
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
	public static boolean loadFromResource(Properties properties,Class<?> context,String name) {
		if(context==null) return false;
		InputStream in=context.getResourceAsStream(name);
		return loadInputStream(properties,in);
	}
	public static boolean loadFromClassLoader(Properties properties,ClassLoader classLoader,String name) {
		if(classLoader==null) return false;
		InputStream in=classLoader.getResourceAsStream(name);
		return loadInputStream(properties,in);
	}
	public static boolean loadFromUrl(Properties properties,URL url) {
		if(url==null) return false;
		try {
			InputStream in=url.openStream();
			return loadInputStream(properties,in);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
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
	public static void printSystemProperties() {
		Properties systemProperties=System.getProperties();
		System.out.println("system properties:");
		System.out.println("system properties size: "+systemProperties.size());
		for(Entry<Object,Object> x:systemProperties.entrySet())
			System.out.println(x.getKey()+"="+x.getValue());
	}
	public static class SortedProperties extends Properties {
		@Override public Set<Object> keySet() {
			return Collections.unmodifiableSet(new TreeSet<Object>(super.keySet()));
		}
		@Override public synchronized Enumeration<Object> keys() {
			return Collections.enumeration(new TreeSet<Object>(super.keySet()));
		}
		private static final long serialVersionUID=1L;
	}
	public static final Properties defaultProperties=new Properties();
	static { /* add some properties */ }

	boolean load_(InputStream in) throws IOException {
		boolean rc=false;
		if(in!=null) {
			properties.load(in);
			in.close();
			rc=true;
		}
		System.out.println(method(3)+" returns: "+rc);
		return rc;
	}
	boolean load(String name) throws IOException {
		InputStream in=getClass().getResourceAsStream(name);
		return load_(in);
	}
	boolean loadcl(String name) throws IOException {
		InputStream in=getClass().getClassLoader().getResourceAsStream(name);
		return load_(in);
	}
	boolean loadurl(String name) throws IOException {
		URL url=getClass().getResource(name);
		if(url!=null) {
			InputStream in=url.openStream();
			return load_(in);
		} else return load_(null);
	}
	boolean loadurlcl(String name) throws IOException {
		URL url=getClass().getClassLoader().getResource(name);
		if(url!=null) {
			InputStream in=url.openStream();
			return load_(in);
		} else return load_(null);
	}
	void run() throws IOException {
		System.out.println("with file: "+dirs+propertyFilename);
		File file=new File(dirs,propertyFilename);
		System.out.println("really: "+file);
		System.out.println(file.exists());
		File file2=new File("src/main/resources/"+dirs,propertyFilename);
		System.out.println(file2+" "+file2.exists());
		for(String filename:filenames) {
			System.out.println("filename: "+filename);
			new P().load(filename);
			new P().loadcl(filename);
			new P().loadurl(filename);
			new P().loadurlcl(filename);
		}
	}
	static String method(int n) {
		return Thread.currentThread().getStackTrace()[n].getMethodName()+"()";
	}
	public static void main(String[] args) throws IOException {
		new P().run();
	}
    public static void oldmain(String[] args) throws Exception {
        SortedProperties properties=new SortedProperties() {
            {
                for(int c=18;c>=0;c--) {
                    String name="Button"+(c<10?"0":"")+c;
                    put(name,name);
                }
            }
            private static final long serialVersionUID=1L;
        };
        StringWriter stringWriter=new StringWriter();
        properties.store(stringWriter,"foo");
        System.err.println(stringWriter.toString());
    }

	Properties properties=new Properties();
	boolean result;
	String string;
	static String propertyFilename="group.properties";
	static String dirs="com/tayek/tablet/";
	static final String[] filenames=new String[] {propertyFilename,dirs+propertyFilename,"/"+propertyFilename,"/"+dirs+propertyFilename};
}
