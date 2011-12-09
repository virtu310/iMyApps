package com.lm.keyrow.rows;

import com.lm.keyrow.KeyRow;

public class Roster implements KeyRow {
	private static final long serialVersionUID = -670421941999141275L;
	
	String uid;
	String roster_uid;
	String group;
	String createTime;
	Integer count;
	
	@Override
	public String key() {
		return getUid() + ':' + getGroup() + ':' + getRosterUid();
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getRosterUid() {
		return roster_uid;
	}
	public void setRosterUid(String ruid) {
		this.roster_uid = ruid;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
