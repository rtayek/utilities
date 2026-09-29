package com.tayek.util.io;
import java.io.*;
import java.util.logging.Logger;
public class Copy implements Runnable {
    public Copy(BufferedReader in,Writer out) { this.in=in; this.out=out; }
    @Override public void run() {
        try {
            boolean once=false;
            while(!done) {
                if(!once) { once=true; }
                String string=in.readLine();
                if(string==null) { done=true; break; }
                out.write(string+'\n');
                out.flush();
            }
            out.flush();
        } catch(Exception e) {
            logger.warning(this+" caught: "+e);
        }
    }
    void run(boolean useThread,boolean join) {
        if(!useThread) {
            run();
        } else {
            Thread thread=new Thread(this,""+useThread);
            thread.start();
            if(join) try {
                thread.join();
            } catch(InterruptedException e) {
                logger.severe("caught: "+e);
            }
        }
    }
    @Override public String toString() { return "Copy [name="+name+", in="+in+", out="+out+"]"; }
    static void sendAndReceive(boolean useThread,BufferedReader r,Writer w) throws IOException {
        Copy rw=new Copy(r,w);
        rw.run(useThread,true);
    }
    public static Writer sendAndReceive(String string,boolean useThread) throws IOException {
        BufferedReader r=toBufferedReader(string);
        Writer w=new StringWriter();
        sendAndReceive(useThread,r,w);
        return w;
    }
    private static BufferedReader toBufferedReader(String string) {
        return new BufferedReader(new StringReader(string));
    }
    String name;
    boolean done;
    Thread thread;
    final BufferedReader in;
    final Writer out;
    private static final Logger logger=Logger.getLogger(Copy.class.getName());
}
