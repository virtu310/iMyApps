package com.lm.keyrow;

import java.util.Iterator;

import java.util.Map;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.lm.keyrow.filters.EqualNotEqualFilter;

public class EqualNotEqualCondition<T> implements Condition<T> {
	T eqRow;
	Map<String, Object> eqMap;
	T neqRow;
	Map<String, Object> neqMap;
	long offset;
	long size;
	
	public EqualNotEqualCondition(T eq, T neq) {
		this(eq, neq, 0, -1);
	}
	
	public EqualNotEqualCondition(T eq, T neq, long size) {
		this(eq, neq, 0, size);
	}
	
	public EqualNotEqualCondition(T row, T neq, long offset, long size) {
		this.eqRow = row;
		this.neqRow = neq;
		this.offset = offset;
		this.size = size;
	}
	
	private Map<String, Object> getEqMap() {
		if (eqMap == null) {
			try {
				eqMap = KeyRowUtils.toMapIgnoreNull(eqRow);
			} catch (KeyRowException e) {
				e.printStackTrace();
			}
		}
		return eqMap;
	}
	
	public Map<String, Object> getNeqMap() {
		if (neqMap == null) {
			try {
				neqMap = KeyRowUtils.toMapIgnoreNull(neqRow);
			} catch (KeyRowException e) {
				e.printStackTrace();
			}
		}
		return neqMap;
	}
	
	@Override
	public Object get(KeyRowAccessor<T> accessor, CharSequence family) throws KeyRowException {
		if (accessor instanceof JVMKeyRowAccessor)
			return new EqualNotEqualFilter<T>(eqRow, neqRow, offset, size);
		else if (accessor instanceof DSKeyRowAccessor) {
			Query query = new Query((String) family);
			if (getEqMap() != null) {
				Iterator<String> iter = getEqMap().keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					query.addFilter(key, FilterOperator.EQUAL, getEqMap().get(key));
				}
			}
			if (getNeqMap() != null) {
				Iterator<String> iter = getNeqMap().keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					query.addFilter(key, FilterOperator.NOT_EQUAL, getNeqMap().get(key));
				}
			}
			return query;
		}
		
		return null;
	}

	@Override
	public long getOffset() {
		return offset;
	}

	@Override
	public long getSize() {
		return size;
	}

}
