package com.tayek.util.io;
import java.io.PrintStream;
import java.util.logging.Logger;
public class Print {
	public static void pn(PrintStream out,String string) {
		out.print(string);
		out.flush();
	}
	public static void p(PrintStream out,String string) {
		synchronized(out) {
			pn(out,string);
			pn(out,System.getProperty("line.separator"));
		}
	}
	public static void p(String string) {
		synchronized(System.out) {
			p(System.out,string);
		}
	}
	public static void pl(Logger logger,String string) {
		if(logger!=null) logger.warning(string);
		p(System.out,string);
	}
}
