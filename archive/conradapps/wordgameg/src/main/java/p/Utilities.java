package p;
public class Utilities {
    static boolean nextCombination(Integer[] v,int N) { //https://stackoverflow.com/a/39844908/51292
        int k=v.length;
        int pivot=k-1;
        while(pivot>=0&&v[pivot]==N-k+pivot)
            --pivot;
        if(pivot==-1) return false;
        ++v[pivot];
        for(int i=pivot+1;i<k;++i)
            v[i]=v[pivot]+i-pivot;
        return true;
    }
    static void printArray(Integer[] a) {
        for(int i=0;i<a.length;i++)
            System.out.print(a[i]+" ");
        System.out.println();
    }
    static int combinations(Integer[] a,int n) {
        int count=0;
        do {
            System.out.print(count+" ");
            printArray(a);
            count++;
        } while(nextCombination(a,n));
        return count;
    }
    public static void main(String[] args) {
        Integer a[]= {0,1,2};
        int count=combinations(a,7);
        System.out.println("Total: "+count);
        Integer b[]= {0,1};
        count=combinations(b,4);
        System.out.println("Total: "+count);
    }
}
