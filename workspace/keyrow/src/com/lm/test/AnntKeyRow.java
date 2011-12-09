package com.lm.test;

import com.lm.keyrow.KeyGetter;
import com.lm.keyrow.ValueGetter;
import com.lm.keyrow.ValueSetter;

public class AnntKeyRow {
	String key;
	String value1;
	Long value2;
	
	@KeyGetter
	@ValueGetter
	public String getKey() {
		return key;
	}
	@ValueSetter
	public void setKey(String key) {
		this.key = key;
	}
	@ValueGetter
	public String getValue1() {
		return value1;
	}
	@ValueSetter
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	@ValueGetter
	public Long getValue2() {
		return value2;
	}
	@ValueSetter
	public void setValue2(Long value2) {
		this.value2 = value2;
	}
	public boolean equals(AnntKeyRow o) {
		if (o.getKey().equals(key) &&
			o.getValue1().equals(value1) &&
			o.getValue2().equals(value2))
			return true;
		
		return false;
	}
}
