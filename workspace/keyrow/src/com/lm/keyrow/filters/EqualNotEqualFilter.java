package com.lm.keyrow.filters;

import java.util.Iterator;
import java.util.Map;

import com.lm.keyrow.KeyRowException;
import com.lm.keyrow.KeyRowUtils;

public class EqualNotEqualFilter<T> extends KeyRowFilter<T> {
	T baserow;
	Map<String, Object> maps;
	T baserow2;
	Map<String, Object> maps2;
	long index = 0;
	long counter = 0;
	long offset;
	long size;
	
	public EqualNotEqualFilter(T eq, T neq) throws KeyRowException {
		this(eq, neq, 0, -1);
	}
	
	public EqualNotEqualFilter(T eq, T neq, long size) throws KeyRowException {
		this(eq, neq, 0, size);
	}
	
	public EqualNotEqualFilter(T eq, T neq, long offset, long size) throws KeyRowException {
		this.baserow = eq;
		this.baserow2 = neq; 
		this.offset = offset;
		this.size = size;
		this.maps = KeyRowUtils.toMapIgnoreNull(eq);
		this.maps2 = KeyRowUtils.toMapIgnoreNull(neq);
	}
	
	@Override
	public int filter(T row) throws KeyRowException {
		Map<String, Object> cmap = KeyRowUtils.toMap(row);
		Iterator<String> keys = maps.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			if (maps.get(key).equals(cmap.get(key)) == false)
				return RETCODE_SKIP;
			if (maps2.get(key).equals(cmap.get(key)))
				return RETCODE_SKIP;
		}
		
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
		if (size >= 0 && ++counter >= size)
			return true;
		return false;
	}

}
