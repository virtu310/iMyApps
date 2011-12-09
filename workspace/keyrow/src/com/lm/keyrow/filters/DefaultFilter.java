package com.lm.keyrow.filters;

import com.lm.keyrow.KeyRowException;

public class DefaultFilter<T> extends KeyRowFilter<T> {
	long index = 0;
	long counter = 0;
	long offset;
	long size;
	
	public DefaultFilter(long size) throws KeyRowException {
		this.offset = 0;
		this.size = size;
	}
	
	public DefaultFilter(long offset, long size) throws KeyRowException {
		this.offset = offset;
		this.size = size;
	}
	
	@Override
	public int filter(T row) throws KeyRowException {
		if (offset > 0 && index++ < offset)
			return RETCODE_NEXTROW;
		
		return RETCODE_INCLUDE;
	}

	@Override
	public boolean hasRemaining() {
		if (size >= 0 && counter >= size)
			return false;
		return true;
	}

	@Override
	public void reset() {
		index = 0;
		counter = 0;
	}

	@Override
	public boolean filterEnd() {
		if (size >= 0 && counter++ >= size)
			return true;
		return false;
	}

}
