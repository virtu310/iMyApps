package com.lm.keyrow.filters;

import com.lm.keyrow.KeyRowException;

abstract public class KeyRowFilter<T> {
	public static int RETCODE_INCLUDE = 1;
	public static int RETCODE_NEXTROW = 2;
	public static int RETCODE_SKIP = 3;
	
	abstract public boolean hasRemaining();
	abstract public boolean filterEnd();
	abstract public int filter(T row) throws KeyRowException;
	abstract public void reset();
}
