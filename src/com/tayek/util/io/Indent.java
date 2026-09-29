package com.tayek.util.io;
public class Indent {
    public Indent(final String string) { this.string=string; }
    public Indent(final Indent indent) { this(indent.string); }
    public void in() { ++indent; }
    public void out() { --indent; }
    public String indent() { return string.repeat(Math.max(0,indent)); }
    private int indent;
    final protected String string;
}
