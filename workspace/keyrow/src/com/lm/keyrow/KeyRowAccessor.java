package com.lm.keyrow;

import java.util.List;

public interface KeyRowAccessor<T> {
	public boolean insert(CharSequence family, T row) throws KeyRowException;
	public boolean remove(CharSequence family, CharSequence key) throws KeyRowException;
	public long remove(CharSequence family, Condition<T> condition) throws KeyRowException;
	public boolean update(CharSequence family, T row) throws KeyRowException;
	public T get(CharSequence family, CharSequence key) throws KeyRowException;
	public List<T> query(CharSequence family, Condition<T> condition) throws KeyRowException;
	public long size(CharSequence family) throws KeyRowException;
	public long size(CharSequence family, Condition<T> condition) throws KeyRowException;
}
