package com.tayek.util.io;
public class Duplex {
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
