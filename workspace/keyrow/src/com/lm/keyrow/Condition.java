package com.lm.keyrow;

public interface Condition<T> {
	public long getOffset();
	public long getSize();
	public Object get(KeyRowAccessor<T> accessor, CharSequence family) throws KeyRowException;
}
