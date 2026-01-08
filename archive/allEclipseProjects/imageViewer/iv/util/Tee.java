package iv.util;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
public class Tee extends FilterOutputStream /* make this into a writer or make a version for writers */ { // tee utility
	Vector stream=new Vector();
	public Tee(final OutputStream out) { super(out); stream.addElement(out); }
	public void teeTo() {
		//Tee tee=new Tee(fileOutputStream);
		//tee.addOutputStream(System.out); /* make into a constructor and put into Tee? */
	}
	public synchronized void addOutputStream(final OutputStream out) { stream.addElement(out); }
	public synchronized void write(final int b) throws IOException {
		for(Enumeration e=stream.elements();e.hasMoreElements();)
		{ final OutputStream out=(OutputStream) e.nextElement(); out.write(b); out.flush(); }
	}
	public synchronized void write(final byte[] data,final int offset,final int length) throws IOException {
		for(Enumeration e=stream.elements(); e.hasMoreElements();)
		{ final OutputStream out=(OutputStream) e.nextElement(); out.write(data, offset, length); out.flush(); }
	}
	public static void tee(final File file) { tee(file,true); }
	public static void tee(final File file,final boolean delete) {
		if(delete&&file.exists())
			file.delete();
		final Tee tee=new Tee(System.out);
		final PrintStream printStream=new PrintStream(tee,true);
		System.setOut(printStream);
		System.setErr(printStream);
		try { tee.addOutputStream(new FileOutputStream(file.toString(),true)); }
		catch(IOException e) { System.out.println(e); }
		System.out.println("tee'd");
	}
	private static final String rcsid="$RCSfile: Tee.java,v $:$Revision: 1.3 $";
}
