package com.tayek.utilities;
public class MyTimer implements Runnable {
    public MyTimer(Runnable runnable,int maxTime) {
        this.runnable=runnable;
        this.maxTime=maxTime;
    }
    @Override public void run() {
        et=new Et();
        runnable.run();
        etms=et.etms();
    }
    public double time() throws InterruptedException  {
        Thread t=new Thread(this);
        t.start();
        t.join(maxTime);
        return (t.isAlive()||etms>maxTime)?Double.NaN:etms;
    }
    public static double time(Runnable runnable,int maxTime) throws InterruptedException {
        return new MyTimer(runnable,maxTime).time();
    }
    static void test(final int sleep,int maxTime,Histogram histogram) throws InterruptedException {
        System.out.print("sleep: "+sleep+", max time: "+maxTime);
        Runnable runnable=new Runnable() {
            @Override public void run() {
                try {
                    Thread.sleep(sleep);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        double time=time(runnable,maxTime);
        System.out.println(" took: "+time+" ms.");
        histogram.add(time);
    }
    public static void main(String[] args) throws InterruptedException {
        Histogram histogram=new Histogram(10,95,105);
        for(int i=0;i<200;i+=20)
            test(100,i,histogram);
        System.out.println(histogram);
        System.out.println(histogram.toStringFrequency());
    }
    Et et;
    final Runnable runnable;
    final int maxTime;
    double etms;
}
