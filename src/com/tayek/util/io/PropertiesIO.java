package com.tayek.util.io;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import com.tayek.util.misc.P;
public class PropertiesIO {
    public static void loadPropertiesFile(Properties properties,String filename) {
        P.loadPropertiesFile(properties,filename);
    }
    public static void loadPropertiesFile(Properties properties,Class<?> context,String filename) {
        if(context!=null) {
            URL url=context.getResource(filename);
            if(url!=null) {
                P.load(properties,filename,url);
                return;
            }
        }
        P.loadPropertiesFile(properties,filename);
    }
    public static void writePropertiesFile(Properties properties,String filename) {
        P.writePropertiesFile(properties,filename);
    }
    public static void writePropertiesFile(Properties properties,Class<?> context,String filename) {
        if(context!=null) {
            URL url=context.getResource(filename);
            if(url!=null&&"file".equals(url.getProtocol())) {
                P.store(new File(url.getPath()),properties);
                return;
            }
        }
        P.writePropertiesFile(properties,filename);
    }
    public static Properties load(final InputStream inputStream) {
        return P.load(inputStream);
    }
    public static void store(final OutputStream outputStream,final Properties properties) {
        P.store(outputStream,properties);
    }
    public static void store(final File propertiesFile,final Properties properties) {
        P.store(propertiesFile,properties);
    }
    public static final Properties defaultProperties=P.defaultProperties;
}
