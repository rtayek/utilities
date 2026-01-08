package com.tayek.util;
public class HistogramExperiment {
    void run(double[] xs) {
        final int n=xs.length;
        printArray("original data (frequencies): ",xs);
        double sum=0;
        for(double x:xs) sum+=x;
        System.out.println("sum: "+sum);
        Histogram histogram=new Histogram(10,0,1);
        histogram.add(xs);
        double mean=histogram.mean();
        double standardDeviation=Math.sqrt(histogram.variance());
        System.out.println("mean="+mean+", sd="+standardDeviation);
        System.out.println("normal histogram of the data: "+histogram);
        double[] xsAsDiscretePMF=new double[n];
        for(int i=0;i<n;i++) xsAsDiscretePMF[i]=xs[i]/sum;
        printArray("data as discrete pmf: ",xsAsDiscretePMF);
        Histogram histogram2=new Histogram(10,0,1);
        histogram2.add(xsAsDiscretePMF);
        System.out.println("histogram as discrete PMF of data: "+histogram2);
        sum=0;
        for(double x:xsAsDiscretePMF) sum+=x;
        System.out.println("sum of data as discrete pmf: "+sum);
        double[] xsNormalized=new double[n];
        for(int i=0;i<n;i++) xsNormalized[i]=(xs[i]-mean)/standardDeviation;
        printArray("normalized data (not frequencies): ",xsNormalized);
        Histogram histogram3=new Histogram(10,0,1);
        histogram3.add(xsNormalized);
        System.out.println("histogram of normalized data: "+histogram2);
    }
    static void printArray(String string,double[] xs) {
        System.out.print(string);
        for(double x:xs) System.out.print(x+",");
        System.out.println();
    }
    public static void main(String[] args) { new HistogramExperiment().run(myEerrors); }
    static double[] myEerrors=new double[] {.1,.2,.2,.2,.3,.3,.3,.3,.3,.4,.4,.4,
            .5,.5,.6,.6,.6,.7,.7,.7,.8,.8,.8,.9};
    static double[] errorsx=new double[] {0.06335141770005678,
            0.06338369378589133,0.0634007770008517,0.06335875243472476,
            0.06337584800407847,0.06340488199528864,0.06336042466221964,
            0.0633256854414784,0.06333516267759356,0.0633833253725439,
            0.06336637192598522,0.06341438585596984,0.06340267785957723,
            0.06331727505851009,};
}
