package com.tayek.util.io;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
public class PropertiesIO {
    private static Properties withDefaults(Properties defaults) {
        Properties properties=new Properties();
        if(defaults!=null) properties.putAll(defaults);
        return properties;
    }
    private static boolean hasPropertiesSource(Class<?> context,String filename) {
        if(context!=null&&context.getResource(filename)!=null) return true;
        return new File(filename).exists();
    }
    private static void loadFromFile(Properties properties,File file) {
        try(InputStream in=new FileInputStream(file)) {
            properties.load(in);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void loadFromUrl(Properties properties,String filename,URL url) {
        if(url!=null) {
            try(InputStream in=url.openStream()) {
                if(in!=null) properties.load(in);
                else System.out.println("properties stream is null for url: "+url);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        } else System.out.println("url is null for filename: "+filename);
    }
    public static void loadPropertiesFile(Properties properties,String filename) {
        loadFromFile(properties,new File(filename));
    }
    public static void loadPropertiesFile(Properties properties,Class<?> context,String filename) {
        if(context!=null) {
            URL url=context.getResource(filename);
            if(url!=null) {
                loadFromUrl(properties,filename,url);
                return;
            }
        }
        loadPropertiesFile(properties,filename);
    }
    public static void writePropertiesFile(Properties properties,String filename) {
        try {
            File file=new File(filename);
            System.out.println("writing new properties to: "+filename+": "+properties);
            properties.store(new FileOutputStream(file),"initial");
        } catch(FileNotFoundException e) {
            System.out.println("properties caught: "+e+" property file was not written!");
        } catch(IOException e) {
            System.out.println("properties caught: "+e+" property file was not written!");
        }
    }
    public static void writePropertiesFile(Properties properties,Class<?> context,String filename) {
        if(context!=null) {
            URL url=context.getResource(filename);
            if(url!=null&&"file".equals(url.getProtocol())) {
                store(new File(url.getPath()),properties);
                return;
            }
        }
        writePropertiesFile(properties,filename);
    }
    public static Properties load(final InputStream inputStream) {
        final Properties properties=new Properties(defaultProperties);
        try {
            properties.load(inputStream);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
    public static void store(final OutputStream outputStream,final Properties properties) {
        try {
            properties.store(outputStream,null);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void store(final File propertiesFile,final Properties properties) {
        try(OutputStream out=new FileOutputStream(propertiesFile)) {
            store(out,properties);
        } catch(FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Properties loadOrCreatePropertiesFile(Properties defaults,String filename) {
        Properties properties=withDefaults(defaults);
        if(new File(filename).exists()) loadPropertiesFile(properties,filename);
        else writePropertiesFile(properties,filename);
        return properties;
    }
    public static Properties loadOrCreatePropertiesFile(Properties defaults,Class<?> context,String filename) {
        Properties properties=withDefaults(defaults);
        if(hasPropertiesSource(context,filename)) loadPropertiesFile(properties,context,filename);
        else writePropertiesFile(properties,context,filename);
        return properties;
    }
    public static final Properties defaultProperties=new Properties();
    static {
        /* add some properties */
    }
}
