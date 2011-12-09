package com.lm.test;

import com.lm.keyrow.KeyRow;

public class KeyRowImpl implements KeyRow {
	String key;
	String value1;
	Integer value2;
	
	public String key() {
		return getKey();
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public Integer getValue2() {
		return value2;
	}
	public void setValue2(Integer value2) {
		this.value2 = value2;
	}
	public boolean equals(KeyRowImpl o) {
		if (o.getKey().equals(key) &&
			o.getValue1().equals(value1) &&
			o.getValue2().equals(value2))
			return true;
		
		return false;
	}
}
