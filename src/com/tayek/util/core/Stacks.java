package com.tayek.util.core;
/**
 * Names of methods on the current call stack, via StackWalker.
 * Indexes follow the old Thread.getStackTrace() convention: 1 is method(int) itself,
 * 2 is the method that called it, 3 is that method's caller, and so on.
 */
public class Stacks {
	private static StackWalker.StackFrame frame(int n) {
		// frame 0 of the walk is frame(int), 1 is method(int)/shortMethod(int), 2 their caller ...
		int skip=Math.max(0,n);
		return StackWalker.getInstance().walk(frames->frames.skip(skip).findFirst()).orElse(null);
	}
	public static String method(int n) {
		StackWalker.StackFrame frame=frame(n);
		return frame==null?"?":frame.getClassName()+'.'+frame.getMethodName()+"()";
	}
	/** The method that called method(). */
	public static String method() {
		return method(3);
	}
	public static String shortMethod(int n) {
		StackWalker.StackFrame frame=frame(n);
		return frame==null?"?":'.'+frame.getMethodName()+"()";
	}
	/** The method that called shortMethod(). */
	public static String shortMethod() {
		return shortMethod(3);
	}
}
