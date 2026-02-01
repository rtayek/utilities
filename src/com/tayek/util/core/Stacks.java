package com.tayek.util.core;
public class Stacks {
	public static String method(int n) {
		return Thread.currentThread().getStackTrace()[n].getClassName()+'.'+Thread.currentThread().getStackTrace()[n].getMethodName()+"()";
	}
	public static String method() {
		return method(2);
	}
	public static String shortMethod(int n) {
		return '.'+Thread.currentThread().getStackTrace()[n].getMethodName()+"()";
	}
	public static String shortMethod() {
		return shortMethod(2);
	}
}
