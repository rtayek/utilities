package com.tayek.util;
public class Semaphore {
    public Semaphore(int n) { this.n=n; }
    public Semaphore() { this(0); }
    synchronized public void p() throws InterruptedException {
        if(n>0) --n;
        else {
            while(n==0) wait();
            --n;
        }
    }
    synchronized public void p2() throws InterruptedException {
        if(n==0) while(n==0) wait(); // not atomic?
        --n;
    }
    synchronized public void v() { ++n; notify(); }
    private int n;
}