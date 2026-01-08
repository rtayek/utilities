package iv.util;
public class time {
	public time(final long t) {
		this.t=t;
	}
	public time() {
		this(System.currentTimeMillis());
	}
	public final long reset(final long t) {
		final long dt=dt();
		this.t=t;
		return dt;
	}
	public final long reset() {
		return reset(System.currentTimeMillis());
	}
	public final long dt() {
		return System.currentTimeMillis()-t;
	}
	public final long dt(final time time) {
		return time.t-this.t;
	}
	private long t;
	private static final String rcsid="$RCSfile: time.java,v $:$Revision: 1.5 $";
}
