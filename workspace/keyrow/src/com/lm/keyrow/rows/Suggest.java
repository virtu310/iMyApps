package com.lm.keyrow.rows;

import com.lm.keyrow.KeyRow;

public class Suggest implements KeyRow {
	private static final long serialVersionUID = 3017786682181343866L;
	public static Suggest INSTANCE = new Suggest();
	
	public static String STATUS_NONE = "none";
	public static String STATUS_NOTUSER = "notuser";
	public static String STATUS_USER = "user";
	
	public static String FROM_EMAIL = "email";
	public static String FROM_MOBILE = "mobile";
	public static String FROM_FACEBOOK = "facebook";
	
	String uid;
	String suggest_uid;
	String from;
	String from_uid;
	String description;
	String relationship;
	String status;
	String createTime;
	
	@Override
	public String key() {
		return uid + ':' + from + ':' + from_uid;
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
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSuggestUid() {
		return suggest_uid;
	}
	public void setSuggestUid(String uid) {
		suggest_uid = uid;
	}
	public String getFromUid() {
		return from_uid;
	}
	public void setFromUid(String fromUid) {
		from_uid = fromUid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
}
