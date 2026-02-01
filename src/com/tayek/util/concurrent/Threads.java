package com.tayek.util.concurrent;
public class Threads {
    public static String toString(Thread thread) {
        return thread.toString()+", state: "+thread.getState()+", is alive: "+thread.isAlive()+", is interrupted:  "+thread.isInterrupted();
    }
    public static Thread[] getThreads() {
        int big=2*Thread.activeCount();
        Thread[] threads=new Thread[big];
        Thread.enumerate(threads);
        return threads;
    }
    public static void printThreads() {
        Thread[] threads=getThreads();
        for(Thread thread:threads)
            if(thread!=null) System.out.println(toString(thread));
    }
}
