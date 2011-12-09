package com.lm.keyrow.rows;

import com.lm.keyrow.KeyRow;

public class ModifyRecord implements KeyRow {
	String uid;
	String date;
	String row_name;
	String key_name;
	String old_value;
	String description;
	
	public String key() {
		return uid + ':' + row_name + ':' + date;
	}
	public String getRowName() {
		return row_name;
	}
	public void setRowName(String name) {
		this.row_name = name;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getKeyName() {
		return key_name;
	}
	public void setKeyName(String name) {
		key_name = name;
	}
	public String getOldValue() {
		return old_value;
	}
	public void setOldValue(String oldValue) {
		old_value = oldValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
