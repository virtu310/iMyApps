package com.lm.keyrow;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KeyRowUtils {
	private static Object EMPTY_ARGS[] = new Object[0];
	private static Class EMPTY_CLASSES[] = new Class[0];

	private static String trimName(String name) {
		if (name.length() > 3 && (name.startsWith("get") || name.startsWith("set"))) {
			return Character.toUpperCase(name.charAt(3)) + name.substring(4);
		}
		return name;
	}
	
	private static String trimGetName(String name) {
		if (name.length() > 3 && name.startsWith("get")) {
			return Character.toUpperCase(name.charAt(3)) + name.substring(4);
		}
		return null;
	}
	
	private static String trimSetName(String name) {
		if (name.length() > 3 && name.startsWith("set")) {
			return Character.toUpperCase(name.charAt(3)) + name.substring(4);
		}
		return null;
	}
	
	private static String keyName4get(Method m) {
		KeyGetter getter = (KeyGetter) m.getAnnotation(KeyGetter.class);
		if (getter != null) {
			String name = getter.name();
			if (name != null && name.length() > 0)
				return name;
		}
		
		return trimName(m.getName());
	}
	
	private static String valueName4get(Method m) {
		ValueGetter getter = (ValueGetter) m.getAnnotation(ValueGetter.class);
		if (getter != null) {
			String name = getter.value();
			if (name != null && name.length() > 0)
				return name;
		}
		
		return trimName(m.getName());
	}

	private static String valueName4set(Method m) {
		ValueSetter setter = (ValueSetter) m.getAnnotation(ValueSetter.class);
		if (setter != null) {
			String name = setter.name();
			if (name != null && name.length() > 0)
				return name;
		}
		
		return trimName(m.getName());
	}

	/**
	 * Get name of the key
	 * @param o
	 * @return the name of the key
	 * @throws KeyRowException
	 */
	public static String getKeyName(Object o) throws KeyRowException {
		if (o instanceof KeyRow)
			return "";
		
		Method methods[] = o.getClass().getMethods();
		
		for (Method method : methods) {
			if (method.isAnnotationPresent(KeyGetter.class)) {
				return keyName4get(method);
			}
		}
		
		throw new KeyRowException("No key found!");
	}
	
	/**
	 * Get key value
	 * @param o
	 * @return the value of key
	 * @throws KeyRowException
	 */
	public static String getKeyValue(Object o) throws KeyRowException {
		if (o instanceof KeyRow)
			return ((KeyRow)o).key();
		
		try {
			Method methods[] = o.getClass().getMethods();
			
			for (Method method : methods) {
				if (method.isAnnotationPresent(KeyGetter.class))
					return method.invoke(o, EMPTY_ARGS).toString();
			}
			
			throw new KeyRowException("No key value found!");
		}
		catch (KeyRowException e) {
			throw e;
		}
		catch (Exception e) {
			throw new KeyRowException(e);
		}
	}
	
	/**
	 * Get value
	 * @param o
	 * @param name the value name
	 * @return
	 * @throws KeyRowException
	 */
	public static Object getValue(Object o, String name) throws KeyRowException {
		try {
			Method methods[] = o.getClass().getMethods();
			
			if (o instanceof KeyRow) {
				for (Method method : methods) {
					String mn = trimGetName(method.getName());
					if (mn != null && mn.equals(name))
						return method.invoke(o, EMPTY_ARGS);
				}
			}
			else {
				for (Method method : methods) {
					if (method.isAnnotationPresent(ValueGetter.class)) {
						String vn = valueName4get(method);
						if (vn.equals(name))
							return method.invoke(o, EMPTY_ARGS);
					}
				}
			}
			
			return null;
		}
		catch (Exception e) {
			throw new KeyRowException(e);
		}
	}
	
	private static void setValue(Method method, Object obj, Object value) throws Exception {
		if (value == null) {
			method.invoke(obj, new Object[]{value});
			return;
		}
		
		Class types[] = method.getParameterTypes();
		
		if (types[0].equals(String.class))
			method.invoke(obj, new Object[]{value.toString()});
		else if (types[0] == Integer.class) {
			if (value instanceof Integer)
				method.invoke(obj, new Object[]{value});
			else
				method.invoke(obj, new Object[]{Integer.parseInt(value.toString())});
		}
		else if (types[0] == Long.class) {
			if (value instanceof Long)
				method.invoke(obj, new Object[]{value});
			else
				method.invoke(obj, new Object[]{Long.parseLong(value.toString())});
		}
		else if (types[0] == Boolean.class) {
			if (value instanceof Boolean)
				method.invoke(obj, new Object[]{value});
			else
				method.invoke(obj, new Object[]{Boolean.parseBoolean(value.toString())});
		}
		else if (types[0] == Double.class) {
			if (value instanceof Double)
				method.invoke(obj, new Object[]{value});
			else
				method.invoke(obj, new Object[]{Double.parseDouble(value.toString())});
		}
	}

	/**
	 * Set value
	 * @param o
	 * @param name the name of the value
	 * @param value which value should be set 
	 * @throws KeyRowException
	 */
	public static void setValue(Object o, String name, Object value) throws KeyRowException {
		try {
			Method methods[] = o.getClass().getMethods();
			
			if (o instanceof KeyRow) {
				for (Method method : methods) {
					String mn = trimSetName(method.getName());
					if (mn != null && mn.equals(name)) {
						setValue(method, o, value);
						return;
					}
				}
			}
			else {
				for (Method method : methods) {
					if (method.isAnnotationPresent(ValueSetter.class)) {
						String vn = valueName4set(method);
						if (vn.equals(name)) {
							setValue(method, o, value);
							return;
						}
					}
				}
			}
		}
		catch (Exception e) {
			throw new KeyRowException(e);
		}
	}
	
	private static Method findSetMethod4Name(String name, Object obj) throws Exception {
		Method methods[] = obj.getClass().getMethods();
		
		if (obj instanceof KeyRow) {
			for (Method method : methods) {
				String mn = trimSetName(method.getName());
				if (mn != null && mn.equals(name))
					return method;
			}
		}
		else {
			for (Method method : methods) {
				if (method.isAnnotationPresent(ValueSetter.class)) {
					if (name.equals(valueName4set(method)))
						return method;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Copy all value from one keyrow to another
	 * @param src
	 * @param dest
	 * @return the dest object
	 * @throws KeyRowException
	 */
	public static Object copy(Object src, Object dest) throws KeyRowException {
		try {
			Method methods[] = src.getClass().getMethods();
			
			if (src instanceof KeyRow) {
				for (Method method : methods) {
					String name = trimGetName(method.getName());
					Method setMethod = findSetMethod4Name(name, dest);
					if (setMethod != null)
						setValue(setMethod, dest, method.invoke(src, EMPTY_ARGS));
				}
			}
			else {
				for (Method method : methods) {
					if (method.isAnnotationPresent(ValueGetter.class)) {
						String name = valueName4get(method);
						Method setMethod = findSetMethod4Name(name, dest);
						if (setMethod != null)
							setValue(setMethod, dest, method.invoke(src, EMPTY_ARGS));
					}
				}
			}
			
			return dest;
		}
		catch (Exception e) {
			throw new KeyRowException(e);
		}
	}
	
	/**
	 * Duplicate one keyrow
	 * @param src
	 * @return
	 * @throws KeyRowException
	 */
	public static Object dup(Object src) throws KeyRowException {
		try {
			Object dest;
			Constructor constructor = src.getClass().getConstructor(EMPTY_CLASSES);
			dest = constructor.newInstance(EMPTY_ARGS);
			return copy(src, dest);
		}
		catch (Exception e) {
			throw new KeyRowException(e);
		}
	}
	
	/**
	 * Put all values to an map
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> toMap(Object obj) throws KeyRowException {
		try {
			Method methods[] = obj.getClass().getMethods();
			Map<String, Object> map = new HashMap<String, Object>();
			
			if (obj instanceof KeyRow) {
				for (Method method : methods) {
					String name = trimGetName(method.getName());
					if (name != null) {
						Class ctype = method.getReturnType();
						if (ctype != String.class &&
								ctype != Integer.class &&
								ctype != Long.class &&
								ctype != Boolean.class &&
								ctype != Double.class)
								continue;
					
						map.put(name, method.invoke(obj, EMPTY_ARGS));
					}
				}
			}
			else {
				for (Method method : methods) {
					if (method.isAnnotationPresent(ValueGetter.class)) {
						String name = valueName4get(method);
						map.put(name, method.invoke(obj, EMPTY_ARGS));
					}
				}
			}
			
			return map;
		}
		catch (Exception e) {
			throw new KeyRowException(e);
		}
	}
	
	/**
	 * Put all values to an map but ignore null value
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> toMapIgnoreNull(Object obj) throws KeyRowException {
		try {
			Method methods[] = obj.getClass().getMethods();
			Map<String, Object> map = new HashMap<String, Object>();
			
			if (obj instanceof KeyRow) {
				for (Method method : methods) {
					String name = trimGetName(method.getName());
					if (name != null) {
						Class ctype = method.getReturnType();
						if (ctype != String.class &&
							ctype != Integer.class &&
							ctype != Long.class &&
							ctype != Boolean.class &&
							ctype != Double.class)
							continue;
						
						Object value = method.invoke(obj, EMPTY_ARGS);
						if (value != null)
							map.put(name, value);
					}
				}
			}
			else {
				for (Method method : methods) {
					if (method.isAnnotationPresent(ValueGetter.class)) {
						String name = valueName4get(method);
						Object value = method.invoke(obj, EMPTY_ARGS);
						if (value != null)
							map.put(name, value);
					}
				}
			}
			
			return map;
		}
		catch (Exception e) {
			throw new KeyRowException(e);
		}
	}

	/**
	 * Put all values from an map to a keyrow
	 * @param map
	 * @param obj
	 * @throws Exception
	 */
	public static void mapTo(Map<String, Object> map, Object obj) throws KeyRowException {
		try {
			Iterator<String> iter = map.keySet().iterator();
			
			while (iter.hasNext()) {
				String name = iter.next();
				Method setMethod = findSetMethod4Name(name, obj);
				if (setMethod != null)
					setValue(setMethod, obj, map.get(name));
			}
		}
		catch (Exception e) {
			throw new KeyRowException(e);
		}
	}
}
