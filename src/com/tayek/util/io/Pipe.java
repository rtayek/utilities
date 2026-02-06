package com.tayek.util.io;
import java.io.*;
import java.util.logging.Logger;
public class Pipe {
    public Pipe(BufferedReader in,Writer out) { this.in=in; this.out=out; }
    public Pipe() {
        PipedOutputStream pos=new PipedOutputStream();
        out=new OutputStreamWriter(pos);
        PipedInputStream input;
        try {
            input=new PipedInputStream(pos);
        } catch(IOException e) {
            logger.warning(this+" caught: "+e);
            throw new RuntimeException(e);
        }
        in=new BufferedReader(new InputStreamReader(input));
    }
    @Override public String toString() { return "Pipe [in="+in+", out="+out+"]"; }
    public final BufferedReader in;
    public final Writer out;
    private static final Logger logger=Logger.getLogger(Pipe.class.getName());
}
