package com.tayek.utilities;
import java.util.List;
public class ListPair<T> {
	public ListPair(List<T> first,List<T> second) {
		this.first=first;
		this.second=second;
	}
	@Override public String toString() {
		return ("("+first+","+second);
	}
	public List<T> first;
	public List<T> second;
}
