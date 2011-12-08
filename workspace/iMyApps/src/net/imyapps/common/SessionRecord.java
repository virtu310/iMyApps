package net.imyapps.common;

import java.io.Serializable;

import com.lm.keyrow.KeyGetter;
import com.lm.keyrow.ValueGetter;
import com.lm.keyrow.ValueSetter;

public class SessionRecord implements Serializable {
	private static final long serialVersionUID = 6502948508571799962L;
	
	String sid;
	String uid;
	Long createTime;
	
	@KeyGetter
	@ValueGetter
	public String getSid() {
		return sid;
	}
	@ValueSetter
	public void setSid(String sid) {
		this.sid = sid;
	}
	@ValueGetter
	public String getUid() {
		return uid;
	}
	@ValueSetter
	public void setUid(String uid) {
		this.uid = uid;
	}
	@ValueGetter
	public Long getCreateTime() {
		return createTime;
	}
	@ValueSetter
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	
}
