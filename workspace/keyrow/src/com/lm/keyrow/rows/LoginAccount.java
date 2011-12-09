package com.lm.keyrow.rows;

import com.lm.keyrow.KeyGetter;
import com.lm.keyrow.KeyRow;
import com.lm.keyrow.ValueGetter;
import com.lm.keyrow.ValueSetter;

public class LoginAccount implements KeyRow {
	private static final long serialVersionUID = -176819702283710688L;
	
	public static LoginAccount INSTANCE = new LoginAccount();
	
	public static String STATUS_NORMAL = "normal";
	public static String STATUS_NEED_VERIFY = "need_verify";
	public static String STATUS_FROZEN = "frozen";
	public static String STATUS_REMOVED = "removed";
	public static String FROM_EMAIL = "email";
	public static String FROM_MOBILE = "mobile";
	public static String FROM_FACEBOOK = "facebook";
	
	String from;
	String from_uid;
	String uid;
	String status;
	String check;
	String createTime;
	String lastLoginTime;
	String updateTime;
	String verifyTime;
	
	public static String key(String from, String uid) {
		return from + ':' + uid;
	}
	
	@Override
	public String key() {
		return from + ':' + from_uid;
	}
	
	@ValueGetter
	public String getStatus() {
		return status;
	}
	
	@ValueSetter
	public void setStatus(String status) {
		this.status = status;
	}
	
	@ValueGetter
	public String getUid() {
		return uid;
	}
	
	@ValueSetter
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@KeyGetter
	@ValueGetter
	public String getFrom() {
		return from;
	}
	
	@ValueSetter
	public void setFrom(String from) {
		this.from = from;
	}
	
	@ValueGetter
	public String getCreateTime() {
		return createTime;
	}
	
	@ValueSetter
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ValueGetter
	public String getLastLoginTime() {
		return lastLoginTime;
	}

	@ValueSetter
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@ValueGetter
	public String getUpdateTime() {
		return updateTime;
	}

	@ValueSetter
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@ValueGetter
	public String getFromUid() {
		return from_uid;
	}

	@ValueSetter
	public void setFromUid(String fuid) {
		this.from_uid = fuid;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(String verifyTime) {
		this.verifyTime = verifyTime;
	}
}
