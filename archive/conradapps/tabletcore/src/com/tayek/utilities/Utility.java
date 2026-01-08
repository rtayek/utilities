package com.tayek.utilities;
import static com.tayek.tablet.Configuration.copiesOfUdpMessages;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import com.tayek.tablet.model.Message;
public class Utility {
    public static int smallest(Message sent) {
        return sent.messageId-copiesOfUdpMessages+1;
    }
    public static int smallest(Message sent,int n) {
        return sent.messageId-n+1;
    }
    static void print(Thread[] threads,int now) {
        System.out.println("threads:");
        for(int i=0;i<now;i++)
            System.out.println(threads[i]);
    }
    public static Pair<Integer,Thread[]> getThreads() {
        int big=2*Thread.activeCount();
        Thread[] threads=new Thread[big];
        int active=Thread.enumerate(threads);
        return new Pair<>(active,threads);
    }
    public static void waitForThreads(int then) {
        boolean ok=false;
        while(!ok) {
            Pair<Integer,Thread[]> pair=getThreads();
            int active=pair.first;
            ok=active<=then;
            if(ok) {
                //System.out.println(active+"-"+then+"="+(active-then));
                //print(threads,then);
                break;
            }
            Thread.yield();
        }
    }
    public static boolean checkAndPrintThreads(int then,boolean print) {
        Pair<Integer,Thread[]> pair=getThreads();
        int now=pair.first;
        boolean ok=true;
        if(then>=0) {
            ok=now<=then;
            if(!ok) System.out.println(now+"-"+then+"="+(now-then)+" extra thread(s).");
            if(!ok||print) print(pair.second,now);
        } else print(pair.second,now);
        return ok;
    }
    public static boolean checkThreads(int threads) {
        return checkAndPrintThreads(threads,false);
    }
    public static void printThreads() {
        checkAndPrintThreads(-1,true);
    }
}
