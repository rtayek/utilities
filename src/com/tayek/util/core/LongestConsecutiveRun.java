package com.tayek.util.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class LongestConsecutiveRun {
    public static int longest(List<Integer> numbers) {
        TreeSet<Integer> sorted = new TreeSet<>(numbers);
        Map<Integer, Integer> map1 = new HashMap<>();
        Map<Integer, Integer> map2 = new HashMap<>();
        for (int n : sorted) {
            // TODO
        }
        return 0;
    }
}
