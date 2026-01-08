package com.tayek.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
// quick and dirty for comparing urls with &'s in them.
public class MySplit {
    static List<String> p(String string,String delimiter) {
        String[] words=string.split(delimiter);
        return Arrays.asList(words);
    }
    public static void main(String[] args) {
        String delimiter=args!=null&&args.length>0&&args[0]!=null?args[0]:"&";
        String s1="https://www.autotrader.com/cars-for-sale/certified-cars/hyundai/tucson/lakewood-ca-90712?dma=&vhrTypes=NO_ACCIDENTS%2CONE_OWNER&searchRadius=100&trimCodeList=TUCSON%7CLimited%2CTUCSON%7CNight%2CTUCSON%7CSE%20Plus%2CTUCSON%7CSEL%20Plus%2CTUCSON%7CSport%2CTUCSON%7CValue&location=&startYear=2017&endYear=2018&marketExtension=include&isNewSearch=false&showAccelerateBanner=false&sortBy=distanceASC&numRecords=25";
        String s2="https://www.autotrader.com/cars-for-sale/all-cars/hyundai/tiburon/lakewood-ca-90712?dma=&searchRadius=100&location=&marketExtension=include&endYear=2019&isNewSearch=true&showAccelerateBanner=false&sortBy=relevance&numRecords=25";
        System.out.println(s1.equals(s2)?"equal":"not equal");
        System.out.println();
        List<String> l1=p(s1,delimiter);
        for(String s:l1) System.out.println(s);
        List<String> l2=p(s2,delimiter);
        for(String s:l2) System.out.println(s);
        List<String> l3=new ArrayList<>();
        l3.addAll(l1);
        l3.addAll(l2);
        System.out.println("------------------");
        Collections.sort(l3);
        for(String s:l3) System.out.println(s);

    }
}
