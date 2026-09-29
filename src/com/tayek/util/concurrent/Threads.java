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
    /** Prints the live threads once a second until no more than n are active (was log.Joiner). */
    public static void waitUntilAtMost(int n) throws InterruptedException {
        while(Thread.activeCount()>n) {
            System.out.println("threads:");
            printThreads();
            Thread.sleep(1_000);
        }
    }
    public static void printThreads() {
        Thread[] threads=getThreads();
        for(Thread thread:threads)
            if(thread!=null) System.out.println(toString(thread));
    }
}
