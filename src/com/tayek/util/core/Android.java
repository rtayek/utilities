package com.tayek.util.core;
public class Android {
	public static boolean isAndroid() {
		return System.getProperty("http.agent")!=null; // fragile!
	}
}
