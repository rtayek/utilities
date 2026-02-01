package com.tayek.util.core;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.IntStream;
public class Texts {
	public static boolean isLineFeedOrCarriageReturn(Character character) {
		return character.equals('\n')||character.equals('\r');
	}
	public static void removeCr(final StringBuffer stringBuffer,final String string) {
		for(int i=0;i<string.length();i++)
			if(string.charAt(i)!='\r') stringBuffer.append(string.charAt(i));
	}
	public static String noEol(String string) {
		String s=string;
		if(s.charAt(s.length()-1)=='\n') s=s.substring(0,s.length()-1);
		if(s.charAt(s.length()-1)=='\r') s=s.substring(0,s.length()-1);
		return s;
	}
	public static String quote(String string) {
		StringBuffer sb=new StringBuffer(string.length());
		for(int i=0;i<string.length();i++) {
			char c=string.charAt(i);
			if(isLineFeedOrCarriageReturn(c)) sb.append("\\\\");
			sb.append(c);
		}
		return sb.toString();
	}
	public static String cat(final String[] data) {
		final StringBuffer sb=new StringBuffer();
		for(int i=0;i<data.length;i++)
			sb.append(data[i]).append('\n');
		return sb.toString();
	}
	public static String cat(final List<String> strings) {
		final StringBuffer sb=new StringBuffer();
		for(String string:strings)
			sb.append(string).append('\n');
		return sb.toString();
	}
	public static void printDifferences(PrintStream ps,String expected,String actual) {
		if(!expected.equals(actual)) {
			ps.println("<<<<<<<<<<<<<<<");
			ps.println("ex: "+expected.length()+" '"+expected+"'");
			ps.println("ac: "+actual.length()+" '"+actual+"'");
			ps.println("ex: "+expected.endsWith("\n")+", ac: "+actual.endsWith("\n"));
			ps.println("ex: "+expected.endsWith("\r\n")+", ac: "+actual.endsWith("\r\n"));
			ps.println(expected.equals(actual));
			byte[] bytes=actual.getBytes();
			byte[] bytes2=actual.getBytes();
			if(expected.length()!=actual.length()) {
				ps.println("ex: "+expected.length()+", ac: "+actual.length());
			}
			for(int i=0;i<Math.min(actual.length(),expected.length());++i) {
				char c=actual.charAt(i);
				char c2=expected.charAt(i);
				if(c!=c2) {
					boolean ok=!Character.isISOControl(c);
					ps.print(i+" "+(ok?c:" ")+" "+(!ok?bytes[i]:""));
					boolean ok2=!Character.isISOControl(c2);
					ps.print(", "+(ok2?c2:" ")+" "+(!ok2?bytes2[i]:""));
					if(c!=c2) ps.println(" not equal!");
					ps.println();
				}
			}
			ps.println(">>>>>>>>>>>>>>>");
		}
	}
	public static boolean areEqual(String string1,String string2) {
		if(string1.length()!=string2.length()) System.out.println("strings have different length!");
		int n=Math.min(string1.length(),string2.length());
		for(int i=0;i<n;++i)
			if(string1.charAt(i)!=string2.charAt(i)) {
				System.err.println("strings differ at character "+i);
				int start=Math.max(0,i-20),end=Math.min(i+20,n-1);
				System.err.print(string1.substring(start,end));
				System.err.print(string2.substring(start,end));
				return false;
			}
		return true;
	}
	public static Byte[] toObjects(byte[] bytes) {
		return IntStream.range(0,bytes.length).mapToObj(i->bytes[i]).toArray(Byte[]::new);
	}
	public static Character[] toObjects(char[] characters) {
		return IntStream.range(0,characters.length).mapToObj(i->characters[i]).toArray(Character[]::new);
	}
}
