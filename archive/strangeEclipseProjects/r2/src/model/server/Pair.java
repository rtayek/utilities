package model.server;
public class Pair<X,Y> {
	public Pair() {
		this.x=null;
		this.y=null;
	}
	public Pair(X x,Y y) {
		this.x=x;
		this.y=y;
	}
	public String toString() {
		return "("+x+","+y+")";
	}
	public final X x;
	public final Y y;
}
