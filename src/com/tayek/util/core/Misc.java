package com.tayek.util.core;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.lang.model.SourceVersion;
public class Misc {
	public static String getString(String key,ResourceBundle resourceBundle) {
		String string=null;
		try {
			string=resourceBundle.getString(key);
		} catch(MissingResourceException e) {}
		return string;
	}
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
