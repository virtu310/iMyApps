package com.lm.keyrow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KR {
	static Map<CharSequence, KeyRowAccessor> map = new HashMap<CharSequence, KeyRowAccessor>();
	static Map<CharSequence, Map<CharSequence, Object>> familyMap = new HashMap<CharSequence, Map<CharSequence, Object>>();
	
	public static <T> KeyRowAccessor<T> getAccessorT(T row) {
		KeyRowAccessor accessor = map.get(row.getClass().getName());
		if (accessor == null) {
			//accessor = new JVMKeyRowAccessor<T>(familyMap);
			accessor = new DSKeyRowAccessor<T>(row.getClass());
			map.put(row.getClass().getName(), accessor);
		}
		return accessor;
	}
	
	public static KeyRowAccessor<Object> getAccessor(Class clazz) {
		KeyRowAccessor<Object> accessor = (KeyRowAccessor<Object>) map.get(clazz.getName());
		if (accessor == null) {
			//accessor = new JVMKeyRowAccessor<Object>(familyMap);
			accessor = new DSKeyRowAccessor<Object>(clazz);
			map.put(clazz.getName(), accessor);
		}
		return accessor;
	}
	
	public static boolean insert(Object row) throws KeyRowException {
		return getAccessor(row.getClass()).insert(row.getClass().getSimpleName(), row);
	}
	
	public static boolean remove(Object row) throws KeyRowException {
		return getAccessor(row.getClass()).remove(row.getClass().getSimpleName(), KeyRowUtils.getKeyValue(row));
	}
	
	public static boolean remove(Class clazz, CharSequence key) throws KeyRowException {
		return getAccessor(clazz).remove(clazz.getSimpleName(), key);
	}
	
	public static long remove(Class clazz, Condition condition) throws KeyRowException {
		return getAccessor(clazz).remove(clazz.getSimpleName(), condition);
	}
	
	public static boolean update(Object row) throws KeyRowException {
		return getAccessor(row.getClass()).update(row.getClass().getSimpleName(), row);	
	}
	
	public static boolean put(Object row) throws KeyRowException {
		if (insert(row))
			return true;
		return update(row);
	}
	
	public static Object get(Class clazz, CharSequence key) throws KeyRowException {
		return getAccessor(clazz).get(clazz.getSimpleName(), key);
	}
	
	public static <T> T getT(T row, CharSequence key) throws KeyRowException {
		return getAccessorT(row).get(row.getClass().getSimpleName(), key);
	}
	
	public static List<Object> query(Class clazz, Condition condition) throws KeyRowException {
		return getAccessor(clazz).query(clazz.getSimpleName(), condition);
	}
	
	public static <T> List<T> queryT(T row, Condition<T> condition) throws KeyRowException {
		return getAccessorT(row).query(row.getClass().getSimpleName(), condition);
	}
	
	public static long size(Class clazz) throws KeyRowException {
		return getAccessor(clazz).size(clazz.getSimpleName()); 
	}
	
	public static long size(Class clazz, Condition condition) throws KeyRowException {
		return getAccessor(clazz).size(clazz.getSimpleName(), condition); 
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public static KeyRowAccessor<Object> getAccessor(CharSequence family, Class clazz) {
		KeyRowAccessor<Object> accessor = (KeyRowAccessor<Object>) map.get(family);
		if (accessor == null) {
			//accessor = new JVMKeyRowAccessor<Object>(familyMap);
			accessor = new DSKeyRowAccessor<Object>(clazz);
			map.put(family, accessor);
		}
		return accessor;
	}

	public static <T> KeyRowAccessor<T> getAccessorT(CharSequence family, T row) {
		KeyRowAccessor accessor = map.get(family);
		if (accessor == null) {
			//accessor = new JVMKeyRowAccessor<T>(familyMap);
			accessor = new DSKeyRowAccessor<T>(row.getClass());
			map.put(family, accessor);
		}
		return accessor;
	}

	public static boolean insert(CharSequence family, Object row) throws KeyRowException {
		return getAccessorT(row).insert(family, row);
	}
	
	public static boolean remove(CharSequence family, Object row) throws KeyRowException {
		return getAccessorT(row).remove(family, KeyRowUtils.getKeyValue(row));
	}
	
	public static boolean remove(CharSequence family, Class clazz, CharSequence key) throws KeyRowException {
		return getAccessor(family, clazz).remove(family, key);
	}
	
	public static long remove(CharSequence family, Class clazz, Condition condition) throws KeyRowException {
		return getAccessor(family, clazz).remove(family, condition);
	}
	
	public static boolean update(CharSequence family, Object row) throws KeyRowException {
		return getAccessorT(row).update(family, row);	
	}
	
	public static boolean put(CharSequence family, Object row) throws KeyRowException {
		if (insert(row))
			return true;
		return update(row);
	}
	
	public static Object get(CharSequence family, Class clazz, CharSequence key) throws KeyRowException {
		return getAccessor(family, clazz).get(family, key);
	}
	
	public static <T> T getT(CharSequence family, T row, CharSequence key) throws KeyRowException {
		return getAccessorT(row).get(family, key);
	}
	
	public static List<Object> query(CharSequence family, Class clazz, Condition condition) throws KeyRowException {
		return getAccessor(family, clazz).query(family, condition);
	}
	
	public static <T> List<T> queryT(CharSequence family, T row, Condition<T> condition) throws KeyRowException {
		return getAccessorT(family, row).query(family, condition);
	}
	
	public static long size(CharSequence family, Class clazz) throws KeyRowException {
		return getAccessor(family, clazz).size(family);
	}
	
	public static long size(CharSequence family, Class clazz, Condition condition) throws KeyRowException {
		return getAccessor(family, clazz).size(family, condition); 
	}
}
