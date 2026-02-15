package com.tayek.util.misc;
import java.util.*;
import java.util.Map.Entry;
public class P {
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
}
