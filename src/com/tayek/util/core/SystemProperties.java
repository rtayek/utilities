package com.tayek.util.core;
import java.util.Map.Entry;
import java.util.Properties;
public class SystemProperties {
    public static void printSystemProperties() {
        Properties systemProperties=System.getProperties();
        System.out.println("system properties:");
        System.out.println("system properties size: "+systemProperties.size());
        for(Entry<Object,Object> x:systemProperties.entrySet())
            System.out.println(x.getKey()+"="+x.getValue());
    }
}
