package com.tayek.util.core;
public interface Callback<T> { // should be Consumer<T>
	void call(T t);
}
