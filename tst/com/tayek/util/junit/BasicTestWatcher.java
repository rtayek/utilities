package com.tayek.util.junit;

import java.util.logging.Logger;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import com.tayek.util.core.Et;

public class BasicTestWatcher extends TestWatcher {
    public BasicTestWatcher() { this(null); }
    public BasicTestWatcher(Class<?> klass) {
        this.klass=klass;
        this.className=klass!=null?klass.getName():null;
    }
    protected void log(String message) { logger.info(message); }
    public String ets() { return "at: "+et; }
    protected String className(Description description) {
        return className!=null?className:description.getClassName();
    }
    @Override protected void starting(Description description) {
        ++tests;
        et.reset();
        if(verbosity)
            log("starting test: "+className(description)+"."+description.getMethodName()+" "+ets());
    }
    @Override protected void finished(Description description) {
        if(verbosity)
            log("finished test: "+className(description)+"."+description.getMethodName()+" "+ets());
    }
    @Override protected void failed(Throwable e,Description description) {
        if(verbosity)
            log("failed test: "+className(description)+"."+description.getMethodName()+" "+e);
    }
    @Override protected void succeeded(Description description) {
        if(verbosity)
            log("succeeded test: "+className(description)+"."+description.getMethodName()+" "+ets());
    }
    public Object key;
    public boolean verbosity=true;
    protected final Et et=new Et();
    public final Class<?> klass;
    private final String className;
    public static int tests;
    protected static final Logger logger=Logger.getLogger(BasicTestWatcher.class.getName());
}
