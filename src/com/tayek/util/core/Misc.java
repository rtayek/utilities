package com.tayek.util.core;
import javax.lang.model.SourceVersion;
public class Misc {
	public static int uniform(final int n) {
		return (int)Math.floor(Math.random()*n);
	}
	public static boolean isValidName(String className) {
		return SourceVersion.isIdentifier(className)&&!SourceVersion.isKeyword(className);
	}
	public static boolean implies(Boolean a,boolean b) {
		return !a|b;
	}
}
