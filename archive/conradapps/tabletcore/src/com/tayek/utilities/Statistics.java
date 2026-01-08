package com.tayek.utilities;
public class Statistics { // probably have to add median back in here, but do it
                          // so both flavors are the same size, since we use
                          // arrays of these
    // ctors & dtor
    public Statistics() {
        initialize();
    }
    // Statistics(Statistics statistics) { copy_(s); }
    // mutators
    public void initialize() {
        n_=0;
        sum_=0.;
        sum2_=0;
        min_=(+1e200);
        max_=(-1e200);
    }
    public void accumulate(double x) {
        ++n_;
        sum_+=x;
        sum2_+=x*x;
        if(x<min_) min_=x;
        if(x>max_) max_=x;
    }
    // operators
    public void accumulate(Statistics statistics) {
        n_+=statistics.n_;
        sum_+=statistics.sum_;
        if(statistics.min_<min_) min_=statistics.min_;
        if(statistics.max_>max_) max_=statistics.max_;
    }
    // accessors
    public int n() {
        return n_;
    }
    public double minimum() {
        return n_==0?NaN:min_;
    }
    public double maximum() {
        return n_==0?NaN:max_;
    }
    public double sum() {
        return n_==0?NaN:sum_;
    }
    public double mean() {
        return n_==0?NaN:sum_/n_;
    }
    public double variance() {
        return n_==0?NaN:(sum2_-sum_*sum_/n_)/n_;
    }
    @Override public String toString() {
        String s="mu="+mean()+", sigma="+Math.sqrt(variance())+", min="+minimum();
        s+=", max="+maximum()+", range="+(maximum()-minimum())+", n="+n_;
        return s;
    }
    // void copy_(const Statistics &s)
    // { n_=s.n_; sum_=s.sum_; sum2_=s.sum2_; min_=s.min_; max_=s.max_; }
    double sum_,sum2_,min_,max_;
    int n_;
    static final Double NaN=Double.NaN;
}
