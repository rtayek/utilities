package com.tayek.util.io;
import java.io.*;
import java.util.function.Function;
import java.util.logging.Logger;
public class Copy implements Runnable {
    public static class CopyBC implements Runnable {
        public CopyBC(BufferedReader in,Writer out) { this.in=in; this.out=out; };
        @Override public void run() {
            boolean once=false;
            try {
                while(!done) {
                    if(!once) { once=true; logger.info("back end read"); }
                    String string=in.readLine();
                    logger.info("back end read: "+string);
                    if(string==null) { done=true; break; }
                    if(process!=null) { boolean ok=this.process.apply(string); if(!ok) done=true; }
                }
                out.close();
            } catch(Exception e) {
                logger.warning(this+" caught: "+e);
            }
        }
        static void foo() throws IOException {
            PipedOutputStream aOut=new PipedOutputStream();
            PipedInputStream bIn=new PipedInputStream(aOut);
            PipedOutputStream bOut=new PipedOutputStream();
            PipedInputStream aIn=new PipedInputStream(bOut);
            BufferedReader frontIn=new BufferedReader(new InputStreamReader(aIn));
            Writer frontOut=new OutputStreamWriter(aOut);
            BufferedReader backIn=new BufferedReader(new InputStreamReader(bIn));
            Writer backOut=new OutputStreamWriter(bOut);
            CopyBC copyBC=new CopyBC(backIn,backOut);
            Function<String,Boolean> p=(string)-> {
                try {
                    logger.info("back end write");
                    copyBC.out.write(string+'\n');
                    copyBC.out.flush();
                    return true;
                } catch(IOException e) {
                    e.printStackTrace();
                }
                return false;
            };
            copyBC.process=p;
            Thread thread=new Thread(copyBC);
            thread.start();
            logger.info("started");
            logger.info("front end write");
            frontOut.write("foo\n");
            frontOut.flush();
            logger.info("front end read");
            String string=frontIn.readLine();
            logger.info("front end read:  "+string);
        }
        boolean done;
        Function<String,Boolean> process;
        Thread thread;
        final BufferedReader in;
        final Writer out;
    }
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
    public static void main(String[] args) throws IOException {
        logger.info("enter main");
        CopyBC.foo();
        logger.info("exit main");
    }
    private static final Logger logger=Logger.getLogger(Copy.class.getName());
}
