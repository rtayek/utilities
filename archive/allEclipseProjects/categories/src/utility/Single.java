package utility;
public class Single<T> {
	public Single(T t0) {
		this.t=t0;
	}
	public String toString() {
		return ""+t;
	}
	public T t;
}
