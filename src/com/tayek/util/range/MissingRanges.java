package com.tayek.util.range;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import static com.tayek.util.range.Range.*;
public class MissingRanges<T extends Comparable<T>>extends MissingABC<T,Range<T>> {
    public MissingRanges(T n) {
        super(n);
    }
    @Override public synchronized void adjust(T n) {
        //if(n<0) throw new MissingException("oops");
        Range<T> range=range(n,n);
        if(n.compareTo(largest)<0) {
            if(missing.contains(n)) {
                missing.remove(n);
                if(!outOfOrder.contains(range)) {
                    outOfOrder.add(range);
                    reduce(outOfOrder);
                } else logger.warning("duplicate out of oreder - may be missed if not in recent!");
            } else {
                logger.warning("error: smaller is not in missing: "+n);
                if(outOfOrderContains(n)) {
                    logger.warning("but it is in out of order: "+n);
                    logger.warning("so it is a duplicate that may be missed if not in recent!t"+n);
                } else {
                    logger.severe("error: so we will add it in: "+n);
                    outOfOrder.add(range);
                    reduce(outOfOrder);
                    // throw new MissingException("error: smaller is not in
                    // missing: "+n);
                }
            }
        } else if(n.equals(largest)) logger.fine("duplicat largest: "+n);
        else {
            T nMinus1=range.sequence(n).previous().value(); // n itself is not missing
            for(T t:range(range.sequence(largest).next().value(),nMinus1))
                if(!missing.add(t)) {
                    logger.severe("error: set already contains: "+t);
                    throw new MissingException("error: set already contains: "+t);
                }
            largest=n;
        }
    }
    @Override synchronized public String toString() {
        return largest+"-"+missing+", ooo: "+outOfOrder;
    }
    public static void main(String[] args) throws IOException {
        Missing<Integer,Range<Integer>> m=Missing.factory.createRanges(0);
        System.out.println(m);
        for(int i=0;i<10;i++) {
            m.adjust(i);
            System.out.println(m);
        }
    }
    private boolean outOfOrderContains(T n) {
        for(Range<T> r:outOfOrder) if(r.contains(n)) return true;
        return false;
    }
    public synchronized Set<T> missing() {
        return missing;
    }
    public synchronized Set<Range<T>> outOfOrder() {
        return outOfOrder;
    }
    private final Set<T> missing=new TreeSet<>();
    public final Set<Range<T>> outOfOrder=new TreeSet<>();
}
