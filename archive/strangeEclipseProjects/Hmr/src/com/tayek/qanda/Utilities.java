package com.tayek.qanda;
import java.util.Arrays;
public class Utilities {
	public static Integer[] getNextState(Integer[] indices, Integer[] limits) {
		for (int i = indices.length - 1; i >= 0; indices[i--] = 0)
			if (indices[i] < limits[i] - 1) {
				indices[i]++;
				return indices;
			}
		return null;
	}
	static void initializeIndices(Integer[] indices) {
		for(int i=0;i<indices.length;i++)
			indices[i]=0;
	}
	static void loop(Integer[] indices, Integer[] limits) {
		while (indices != null) {
			System.out.println(Arrays.asList(indices));
			indices = getNextState(indices, limits);
		}
	}
}
