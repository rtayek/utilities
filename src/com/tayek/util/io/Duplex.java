package com.tayek.util.io;
import java.io.BufferedReader;
import java.io.Writer;
public class Duplex {
    public static class End {
        public End(BufferedReader in,Writer out) { this.in=in; this.out=out; }
        public BufferedReader in() { return in; }
        public Writer out() { return out; }
        @Override public String toString() { return "End [in="+in+", out="+out+"]"; }
        public final BufferedReader in;
        public final Writer out;
    }
    public Duplex() {
        Pipe p1=new Pipe();
        Pipe p2=new Pipe();
        front=new End(p1.in,p2.out);
        back=new End(p2.in,p1.out);
    }
    @Override public String toString() {
        return "Duplex [front="+front+", back="+back+", name="+name+"]";
    }
    public String getName() { return name; }
    public void setName(String name) { this.name=name; }
    public final End front,back;
    private transient String name="unnamed";
}
