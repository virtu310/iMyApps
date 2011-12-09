package com.lm.keyrow.rows;

import com.lm.keyrow.KeyRow;

public class ReverseRoster implements KeyRow {
	private static final long serialVersionUID = 6377678860809022734L;
	
	String uid;
	String ruid;
	
	public String key() {
		return uid + ':' + ruid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getRUid() {
		return ruid;
	}
	public void setRUid(String ruid) {
		this.ruid = ruid;
	}
}
