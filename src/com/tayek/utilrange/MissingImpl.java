package com.tayek.utilrange;
import static com.tayek.utilrange.Range.*;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
abstract class MissingABC<T extends Comparable<T>,R> implements Missing<T,R> {
    MissingABC(T t) {
        range=range(t,t);
        largest=range.sequence(t).previous().value();
    }
    @Override public T largest() {
        return largest;
    }
    @Override public synchronized boolean isMissing(T n) {
        //if(n<0) throw new MissingException("oops");
        return missing().contains(n);
    }
    @Override public synchronized boolean areAnyMissing() {
        return missing().size()>0;
    }
    @Override public synchronized boolean areAnyOutOfOrder() {
        return outOfOrder().size()>0;
    }
    @Override public synchronized boolean isDuplicate(T n) {
        //if(n<0) throw new MissingException("oops");
        if(n.compareTo(largest)<0&&!isMissing(n)) {
            logger.severe(n+" is strange duplicate: "+this); // getting this on tablets
            System.err.flush();
        }
        return n.compareTo(largest)<0&&!isMissing(n); // maybe should just return n<largest?
    }
    protected static final Logger logger=Logger.getLogger(MissingABC.class.getName());
    protected T largest;
    protected Range<T> range; // initial value
}
public class MissingImpl<T extends Comparable<T>>extends MissingABC<T,T> { // tracks missing messages from consecutive messages.
    public MissingImpl(T t) { // expect to start with n.
        super(t);
    }
    @Override public synchronized void adjust(T n) {
        //if(n<0) throw new MissingException("oops");
        logger.fine("adjust: "+n+" "+this);
        if(n.compareTo(largest)<0) {
            if(missing.contains(n)) {
                missing.remove(n);
                if(!outOfOrder.contains(n)){
                    logger.info("adjust: adding: "+n+" to: "+this);
                    outOfOrder.add(n);
                }
                else logger.warning("duplicate out of oreder - may be missed if not in recent!");
            } else {
                logger.warning("error: smaller is not in missing: "+n);
                if(outOfOrder.contains(n)) {
                    logger.warning("but it is in out of order: "+n);
                    logger.warning("so it is a duplicate that may be missed if not in recent!t"+n);
                } else {
                    logger.severe("error: so we will add it in: "+n);
                    outOfOrder.add(n);
                    // throw new MissingException("error: smaller is not in
                    // missing: "+n);
                }
            }
        } else if(n.equals(largest)) logger.fine("duplicat largest: "+n);
        else {
            T nMinus1=range(n,n).sequence(n).previous().value();
            logger.fine("range missing: "+range(range.sequence(largest).next().value(),nMinus1));
            for(T t:range(range.sequence(largest).next().value(),nMinus1)) {
                logger.fine("adding missing: "+t+" to: "+this);
                if(!missing.add(t)) {
                    logger.severe("error: set already contains: "+t);
                    //throw new MissingException("error: set already contains: "+t);
                }
            }
            largest=n;
        }
        logger.fine("after adjust: "+n+" "+this);
    }
    @Override synchronized public String toString() {
        return largest+"-"+missing+", ooo: "+outOfOrder;
    }
    public synchronized Set<T> missing() {
        return missing;
    }
    public synchronized Set<T> outOfOrder() {
        return outOfOrder;
    }
    public static void main(String[] args) throws IOException {
        Missing<Integer,Integer> m=Missing.factory.createNormal(0);
        System.out.println(m);
        for(int i=0;i<10;i++) {
            m.adjust(i);
            System.out.println(m);
        }
    }
    private final Set<T> missing=new TreeSet<>();
    public final Set<T> outOfOrder=new TreeSet<>();
}
