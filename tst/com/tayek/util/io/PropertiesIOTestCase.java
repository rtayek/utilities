package com.tayek.util.io;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.junit.Test;
public class PropertiesIOTestCase {
    @Test public void testLoadOrCreatePropertiesFileCreatesMissingFile() throws IOException {
        File file=File.createTempFile("properties-io-create",".properties");
        file.deleteOnExit();
        assertTrue(file.delete());
        Properties defaults=new Properties();
        defaults.setProperty("alpha","1");
        Properties properties=PropertiesIO.loadOrCreatePropertiesFile(defaults,file.getAbsolutePath());
        assertTrue(file.exists());
        assertEquals("1",properties.getProperty("alpha"));
    }
    @Test public void testLoadOrCreatePropertiesFileMergesDefaultsAndExisting() throws IOException {
        File file=File.createTempFile("properties-io-merge",".properties");
        file.deleteOnExit();
        Properties existing=new Properties();
        existing.setProperty("beta","2");
        PropertiesIO.writePropertiesFile(existing,file.getAbsolutePath());
        Properties defaults=new Properties();
        defaults.setProperty("alpha","1");
        Properties properties=PropertiesIO.loadOrCreatePropertiesFile(defaults,file.getAbsolutePath());
        assertEquals("1",properties.getProperty("alpha"));
        assertEquals("2",properties.getProperty("beta"));
    }
    @Test public void testLoadOrCreatePropertiesFileWithContextFallsBackToFile() throws IOException {
        File file=File.createTempFile("properties-io-context",".properties");
        file.deleteOnExit();
        assertTrue(file.delete());
        Properties defaults=new Properties();
        defaults.setProperty("alpha","1");
        Properties properties=PropertiesIO.loadOrCreatePropertiesFile(defaults,PropertiesIOTestCase.class,file.getAbsolutePath());
        assertTrue(file.exists());
        assertEquals("1",properties.getProperty("alpha"));
    }
}
