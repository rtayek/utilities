package sgf;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;
import io.Logging;
public class Tee extends FilterOutputStream {
    // make this into a writer or make a version for writers
    // not a good idea unless we use the apache thing
    public Tee(OutputStream out) { super(out); stream.addElement(out); }
    public void teeTo() {
        // Tee tee=new Tee(fileOutputStream);
        // tee.addOutputStream(System.out); /* make into a constructor and put
        // into Tee? */
        // seems like we don't have a need for this yet
    }
    public synchronized void addOutputStream(OutputStream out) { stream.addElement(out); }
    @Override public synchronized void write(int b) throws IOException {
        for(Enumeration<OutputStream> e=stream.elements();e.hasMoreElements();) {
            OutputStream out=e.nextElement();
            out.write(b);
            out.flush();
        }
    }
    @Override public synchronized void write(byte[] data,int offset,int length) throws IOException {
        for(Enumeration<OutputStream> e=stream.elements();e.hasMoreElements();) {
            OutputStream out=e.nextElement();
            out.write(data,offset,length);
            out.flush();
        }
    }
    public static Tee tee(File file) { return tee(file,true); }
    public static Tee tee(File file,boolean delete) {
        if(delete&&file.exists()) if(!file.delete()) System.out.println(file+" was not deleted!");
        else System.out.println(file+" was deleted.");
        Tee tee=new Tee(System.out); // first is system.out
        try {
            tee.addOutputStream(new FileOutputStream(file.toString(),true));
            PrintStream printStream=new PrintStream(tee,true);
            System.setOut(printStream);
            System.setErr(printStream);
            Logging.logger.fine("tee'd");
            //System.out.println("tee'd");
            return tee;
        } catch(IOException e) {
            Logging.logger.warning("tee caught: "+e);
            return null;
        }
    }
    Vector<OutputStream> stream=new Vector<OutputStream>();
}