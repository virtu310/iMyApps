package com.lm.cache;

public interface Cache<T> {
	public void clearAll();
	public void setNamespace(CharSequence namespace);
	public CharSequence getNamespace();
	public T get(CharSequence key);
	public void put(CharSequence key, T value);
	public void put(CharSequence key, T value, long expire_ms);
	public boolean delete(CharSequence key);
}
