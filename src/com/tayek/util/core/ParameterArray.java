package com.tayek.util.core;
import java.util.*;
import java.util.logging.Logger;
public class ParameterArray {
    public static Collection<Object[]> modulo(int n) {
        List<Object[]> parameterArrays=new ArrayList<>();
        for(int i=0;i<n;++i) parameterArrays.add(new Object[] {i});
        return parameterArrays;
    }
    public static Collection<Object[]> parameterize(Collection<?> objects) {
        List<Object[]> parameterArrays=new ArrayList<>();
        for(Object object:objects) parameterArrays.add(new Object[] {object});
        return parameterArrays;
    }
    public static Collection<Object[]> parameterize(Object... objects) {
        return parameterize(Arrays.asList(objects));
    }
    public static void main(String[] args) {
        Collection<Object[]> collection=modulo(3);
        for(Object[] o:collection) {
            List<Object> x=Arrays.asList(o);
            logger.info(String.valueOf(x));
        }
    }
    private static final Logger logger=Logger.getLogger(ParameterArray.class.getName());
}
