package com.tayek.util.core;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Logger;
public class Range<T extends Comparable<T>> {
    static class Ranges extends TreeSet<Range<?>> {
        private static final long serialVersionUID=1L;
    }
    public static <T extends Number & Comparable<T>> int compare(T a,T b) { return a.compareTo(b); }
    public Range(T min,T max) { this.min=min; this.max=max; }
    boolean contains(T t) {
        if(min instanceof Number) {
            int rc1=numberComparator.compare((Number)min,(Number)t);
            int rc2=numberComparator.compare((Number)t,(Number)max);
            if(rc1<=0&&rc2<=0) return true;
        } else if(min instanceof Enum<?>) {
            Class<?> clazz=((Enum<?>)t).getDeclaringClass();
            if(clazz!=null) {
                List<?> enums=Arrays.asList(clazz.getEnumConstants());
                if(enums.contains(t))
                    if(((Enum<?>)min).ordinal()<=((Enum<?>)t).ordinal()
                            &&((Enum<?>)t).ordinal()<=((Enum<?>)max).ordinal())
                        return true;
            }
        }
        return false;
    }
    enum L { a, b, c, d, e }
    public static void main(String[] args) {
        Range<Integer> ir=new Range<>(0,10);
        logger.info(String.valueOf(ir.contains(-1)));
        logger.info(String.valueOf(ir.contains(0)));
        logger.info(String.valueOf(ir.contains(1)));
        logger.info(String.valueOf(ir.contains(10)));
        logger.info(String.valueOf(ir.contains(11)));
        Range<L> er=new Range<>(L.b,L.e);
        logger.info(String.valueOf(er.contains(L.a)));
        logger.info(String.valueOf(er.contains(L.b)));
        logger.info(String.valueOf(er.contains(L.c)));
        logger.info(String.valueOf(er.contains(L.e)));
    }
    final T min,max;
    static final Comparator<Number> numberComparator=(a,b)->Double.compare(a.doubleValue(),b.doubleValue());
    private static final Logger logger=Logger.getLogger(Range.class.getName());
}
