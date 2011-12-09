package com.lm.keyrow.rows;

import com.lm.keyrow.KeyRow;

public class SignUp implements KeyRow {
	private static final long serialVersionUID = 712053202064447983L;
	public static SignUp INSTANCE = new SignUp();
	
	public static String STATUS_WAITING = "waiting";
	public static String STATUS_MATCHING = "matching";
	public static String STATUS_INVITING = "inviting";
	public static String STATUS_INVITE_ACCEPTED = "invite_accepted";
	public static String STATUS_INVITE_NOT_ACCEPTED = "invite_not_accepted";
	public static String STATUS_CLOSE = "close";
	public static String STATUS_CANCEL = "cancel";
	public static String STATUS_INACTIVE = "inactive";
	
	String sid;
	String uid;
	String invite_uid;
	String invite_text;
	String location;
	Boolean singleLimit;
	Boolean telauthLimit;
	Boolean male;
	Boolean female;
	String status;
	String aid;
	String tags;
	String start_time;
	String end_time;
	Integer price_low;
	Integer price_high;
	String plan;
	String ip_address;
	String updateTime;
	String createTime;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		SignUp s = new SignUp();
		s.sid = this.sid;
		s.uid = this.uid;
		s.invite_uid = this.invite_uid;
		s.invite_text = this.invite_text;
		s.location = this.location;
		s.singleLimit = this.singleLimit;
		s.telauthLimit = this.telauthLimit;
		s.male = this.male;
		s.female = this.female;
		s.status = this.status;
		s.aid = this.aid;
		s.tags = this.tags;
		s.start_time = this.start_time;
		s.end_time = this.end_time;
		s.price_low = this.price_low;
		s.price_high = this.price_high;
		s.plan = this.plan;
		s.ip_address = this.ip_address;
		s.updateTime = this.updateTime;
		s.createTime = this.createTime;
		return s;
	};
	
	public SignUp duplicate() {
		try {
			return (SignUp) clone();
		}
		catch (Exception e){
			return null;
		}
	}
	
	@Override
	public String key() {
		return getSid();
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Boolean getSingleLimit() {
		return singleLimit;
	}
	public void setSingleLimit(Boolean singlelimit) {
		this.singleLimit = singlelimit;
	}
	public Boolean getTelauthLimit() {
		return telauthLimit;
	}
	public void setTelauthLimit(Boolean telauthlimit) {
		this.telauthLimit = telauthlimit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Boolean getMale() {
		return male;
	}
	public void setMale(Boolean male) {
		this.male = male;
	}
	public Boolean getFemale() {
		return female;
	}
	public void setFemale(Boolean female) {
		this.female = female;
	}
	public Integer  getPriceLow() {
		return price_low;
	}
	public void setPriceLow(Integer priceLow) {
		price_low = priceLow;
	}
	public Integer  getPriceHigh() {
		return price_high;
	}
	public void setPriceHigh(Integer priceHigh) {
		price_high = priceHigh;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public String getStartTime() {
		return start_time;
	}
	public void setStartTime(String startTime) {
		start_time = startTime;
	}
	public String getEndTime() {
		return end_time;
	}
	public void setEndTime(String endTime) {
		end_time = endTime;
	}
	public String getIpAddress() {
		return ip_address;
	}
	public void setIpAddress(String ipAddress) {
		ip_address = ipAddress;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getInviteUid() {
		return invite_uid;
	}
	public void setInviteUid(String inviteUid) {
		invite_uid = inviteUid;
	}
	public String getInviteText() {
		return invite_text;
	}
	public void setInviteText(String inviteText) {
		invite_text = inviteText;
	}
}
