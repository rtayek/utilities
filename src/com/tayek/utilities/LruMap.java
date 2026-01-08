package com.tayek.utilities;
import java.util.LinkedHashMap;
import java.util.Map;
public class LruMap<K,V> extends LinkedHashMap<K,V> {
    private static final long serialVersionUID=1L;
    public static final int defaultMaxSize=100;
    public LruMap() {
        this(defaultMaxSize);
    }
    public LruMap(int max) {
        this(max,.75f,true);
    }
    public LruMap(int max,float loadFactor) {
        this(max,loadFactor,true);
    }
    public LruMap(int max,float loadFactor,boolean accessOrder) {
        super(max+1,loadFactor,accessOrder);
        if(!accessOrder) throw new RuntimeException("you did not construct an lru map!");
        this.max=max;
    }
    public LruMap(Map<? extends K,? extends V> map) {
        super(map);
        throw new RuntimeException("you did not construct an lru map!");
    }
    @Override protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size()>max;
    }
    public final int max;
}
