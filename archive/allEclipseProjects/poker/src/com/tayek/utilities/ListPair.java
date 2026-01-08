package com.tayek.utilities;
import java.util.List;
public record ListPair<T>(List<T> first,List<T> second) {
	@Override public String toString() {
		return ("("+first+","+second);
	}
}
