package utility;
import static utility.Finite.*;
import java.util.*;
public class Pair<K,V> implements Map.Entry<K,V> {
	public Pair(K key,V value) {
		this.key=key;
		this.value=value;
	}
	public Pair(Pair<K,V> pair) {
		this(pair.key,pair.value);
	}
	public Pair<V,K> swap() {
		return new Pair<V,K>(value,key);
	}
	@Override public K getKey() {
		return key;
	}
	@Override public V getValue() {
		return value;
	}
	@Override public V setValue(V value) {
		V old=this.value;
		this.value=value;
		return old;
	}
	@Override public int hashCode() {
		final int prime=31;
		int result=1;
		result=prime*result+((key==null)?0:key.hashCode());
		result=prime*result+((value==null)?0:value.hashCode());
		return result;
	}
	@Override public boolean equals(Object obj) {
		if(this==obj) return true;
		if(obj==null) return false;
		if(getClass()!=obj.getClass()) return false;
		@SuppressWarnings("unchecked") Pair<K,V> other=(Pair<K,V>)obj; // ??? //
																		// ok?
		if(key==null) {
			if(other.key!=null) return false;
		} else if(!key.equals(other.key)) return false;
		if(value==null) {
			if(other.value!=null) return false;
		} else if(!value.equals(other.value)) return false;
		return true;
	}
	@Override public String toString() {
		return "("+key+","+value+")";
	}
	public static void main(String[] arguments) {
		final List<Pair<Integer,Integer>> pairs1=new ArrayList<>(3);
		Pair<Integer,Integer> p1=new Pair<>(0,2);
		Pair<Integer,Integer> p2=new Pair<>(1,2);
		Pair<Integer,Integer> p3=new Pair<>(2,2);
		pairs1.add(p1);
		pairs1.add(p2);
		pairs1.add(p3);
		System.out.println("pairs1: "+pairs1);
			for(List<Integer> p:permutations(3)) {
				System.out.println("-------------");
				System.out.println("p2="+p);
				List<Pair<Integer,Integer>> pairs2=new ArrayList<>(pairs1.size());
				for(Pair<Integer,Integer> pair:pairs1) {
					Pair<Integer,Integer> newP=new Pair<>(map(pair.key,p),map(pair.value,p));
					pairs2.add(newP);
				}
				System.out.println(pairs2.equals(pairs1));
				if(pairs2.equals(pairs1)) {
					System.out.println("pairs1: "+pairs1);
					System.out.println("pairs2: "+pairs2);
				}
				else System.out.println("not equals");
			}
	}
	public K key;
	public V value;
}