package com.tayek.util.io;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;
import com.tayek.util.core.Pair;
public class End {
    public End(BufferedReader in,Writer out) { this.in=in; this.out=out; this.socket=null; }
    public End(Socket socket) { this.in=in_(socket); this.out=out_(socket); this.socket=socket; }
    public End(String string,Writer out) { this(toBufferedReader(string),out); }
    public End(String string) { this(toBufferedReader(string),new StringWriter()); }
    public Socket socket() { return socket; }
    public boolean send(String string) { return false; }
    public String receive() { return null; }
    public void close() throws IOException { in.close(); out.close(); }
    public void stop() {
        isStopping=true;
        closeQuietly();
    }
    public BufferedReader in() { return in; }
    public Writer out() { return out; }
    public Thread thread() { return thread; }
    public boolean isStopping() { return isStopping; }
    public boolean setIsStopping() { boolean rc=isStopping; isStopping=true; return rc; }
    public String name() { return name; }
    public void setName(String name) { this.name=name; }
    @Override public String toString() {
        return "End [isStopping="+isStopping+", socket="+socket+", in="+in+", out="+out+", thread="+thread+", name="
                +name+"]";
    }
    private void closeQuietly() {
        try {
            if(out!=null) out.close();
        } catch(IOException e) {
            logger.warning("close out caught: "+e);
        }
        try {
            if(in!=null) in.close();
        } catch(IOException e) {
            logger.warning("close in caught: "+e);
        }
        if(socket!=null&&!socket.isClosed()) try {
            socket.close();
        } catch(IOException e) {
            logger.warning("close socket caught: "+e);
        }
    }
    boolean isStopping;
    protected transient String sent,received;
    public final BufferedReader in;
    public final Writer out;
    public Thread thread;
    public final Socket socket;
    String name;
    public static class Holder { // holds one or both ends of a socket or a two way pipe.
        private Holder(End front,End back) { this.front=front; this.back=back; }
        @Override public String toString() { return "Holder [front="+front+", back="+back+"]"; }
        public static Holder frontEnd(End end) { return new Holder(end,null); }
        private static Holder remoteBackEnd(int port) {
            Holder holder=null;
            Socket backEnd=new Socket();
            boolean ok=connect(host,port,100,backEnd);
            if(ok) holder=new Holder(null,new End(backEnd));
            else {
                try {
                    backEnd.close();
                } catch(IOException e) {
                    // ignore
                }
                throw new RuntimeException("connect fails!");
            }
            return holder;
        }
        public static Holder trick(int port) { // local connection
            End front=null,back=null;
            try(ServerSocket serverSocket=new ServerSocket(port);) {
                Socket frontEnd=new Socket();
                if(frontEnd==null) throw new RuntimeException("socket is null!");
                boolean ok=connect("localhost",serverSocket.getLocalPort(),100,frontEnd);
                if(!ok) throw new RuntimeException("can not connect!");
                Socket backEnd=serverSocket.accept();
                if(backEnd==null) throw new RuntimeException("backend is null!");
                front=new End(frontEnd);
                back=new End(backEnd);
                serverSocket.close();
            } catch(Exception e) {
                logger.warning("in create(), caught: "+e);
                throw new RuntimeException("create can not connect!");
            }
            return new Holder(front,back);
        }
        public static synchronized Holder duplex() { // two way pipe
            Duplex duplex=new Duplex();
            End front=new End(duplex.front.in(),duplex.front.out());
            End back=new End(duplex.back.in(),duplex.back.out());
            return new Holder(front,back);
        }
        public static synchronized Holder create(int port) {
            if(port==noPort) return duplex();
            else if(port==anyPort) return trick(port);
            else { // some real port number >0.
                return remoteBackEnd(port);
            }
        }
        public final End front,back;
    }
    public static class Holders extends Pair<Holder,Holder> {
        public Holders(Holder first,Holder second) { super(first,second); }
        public static Holders create(int port) { return new Holders(Holder.create(port),Holder.create(port)); }
        public static Holders holders(int port) {
            Holder blackHolder=null;
            Holder whiteHolder=null;
            if(port==noPort) {
                blackHolder=Holder.duplex();
                whiteHolder=Holder.duplex();
            } else if(port==anyPort) {
                blackHolder=Holder.trick(port);
                whiteHolder=Holder.trick(port);
            } else {
                blackHolder=Holder.trick(port);
                whiteHolder=Holder.trick(port);
            }
            return new Holders(blackHolder,whiteHolder);
        }
    }
    public static boolean connect(String host,int port,int timeout,Socket socket) {
        if(socket==null) throw new RuntimeException("socket is null!");
        InetSocketAddress inetSocketAddress=new InetSocketAddress(host,port);
        for(int i=0;i<10;++i) {
            String string=connect_(timeout,socket,inetSocketAddress);
            if(string.equals("")) return true;
        }
        return false;
    }
    private static String connect_(int timeout,Socket socket,InetSocketAddress inetSocketAddress) {
        String string="";
        try {
            socket.connect(inetSocketAddress,timeout);
        } catch(Exception e) {
            string+=e;
            logger.warning("connect caught: "+e+" on: "+inetSocketAddress);
        }
        return string;
    }
    private static BufferedReader in_(Socket socket) {
        try {
            return new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static Writer out_(Socket socket) {
        try {
            return new OutputStreamWriter(socket.getOutputStream());
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static BufferedReader toBufferedReader(String string) {
        return new BufferedReader(new StringReader(string));
    }
    public static final int anyPort=0,noPort=-1;
    public static final String host="localhost";
    private static final Logger logger=Logger.getLogger(End.class.getName());
}
