package iv.util;
public class GC {
	public GC(final String string,final M mBefore) {
		this.string=string;
		this.mBefore=mBefore;
	}
	public GC(final String string) {
		this(string,new M());
	}
	public void after() {
		dtBeforeGc=time.dt();
		System.gc();
		dtAfterGc=time.dt();
		after(new M());
	}
	public void after(final M mAfter) {
		this.mAfter=mAfter;
	}
	public void print() {
		System.out.println(string);
		System.out.println("\tbefore total="+mBefore.total+", free="+mBefore.free+", used="+(mBefore.total-mBefore.free)+", dt before gc="+dtBeforeGc);
		System.out.println("\tafter  total="+mAfter.total+", free="+mAfter.free+", used="+(mAfter.total-mAfter.free)+", dt after gc="+dtAfterGc);
		System.out.println("\tdelta  total="+(mAfter.total-mBefore.total)+", free="+(mAfter.free-mBefore.free)+", used="+((mAfter.total-mAfter.free)-(mBefore.total-mBefore.free))+", dt="+(dtAfterGc-dtBeforeGc));
	}
	public static void main(final String[] argument) {
	}
	final String string;
	final M mBefore;
	private M mAfter;
	final time time=new time();
	private long dtBeforeGc,dtAfterGc;
	private static final String rcsid="$RCSfile: GC.java,v $:$Revision: 1.5 $";
}
