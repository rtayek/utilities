package iv.util;
public class M {
	M() {
		total=runtime.totalMemory();
		free=runtime.freeMemory();
	}
	final long total,free;
	static final Runtime runtime=Runtime.getRuntime();
	private static final String rcsid="$RCSfile: M.java,v $:$Revision: 1.6 $";
}
