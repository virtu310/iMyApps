package com.lm.keyrow.rows;

import com.lm.keyrow.KeyRow;

public class ActionRecord implements KeyRow {
	private static final long serialVersionUID = 7640858141183343260L;
	
	public static String STATE_ACTION = "action";
	public static String STATE_CONFIRMED = "confirmed";
	public static String STATE_ABSENCE = "absence";
	public static String STATE_RATED = "rated";
	public static String STATE_NONRATE = "nonrate";
	
	public static String RATING_GOOD = "good";
	public static String RATING_NORMAL = "normal";
	public static String RATING_BAD = "bad";
	
	String uid;
	String aid;
	String name;
	String selected_date1;
	String selected_date2;
	Double cba;
	String attend_list;
	String create_time;
	String close_time;
	String state;
	String rating;
	Boolean finished;
	
	/**
	 * Close ActionRecord 時會 select 所有 finished 的 ActionRecord，若有 6 個代表所有人都完成任務了
	 * @return
	 */
	public Boolean getFinished() {
		return finished;
	}
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
	public String key() {
		return aid + ':' + uid;
	}
	public String getCreateTime() {
		return create_time;
	}
	public void setCreateTime(String createTime) {
		this.create_time = createTime;
	}
	public String getCloseTime() {
		return close_time;
	}
	public void setCloseTime(String closeTime) {
		this.close_time = closeTime;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getSelectedDate1() {
		return selected_date1;
	}
	public void setSelectedDate1(String selectedDate) {
		selected_date1 = selectedDate;
	}
	public String getSelectedDate2() {
		return selected_date2;
	}
	public void setSelectedDate2(String selectedDate) {
		selected_date2 = selectedDate;
	}
	public Double getCba() {
		return cba;
	}
	public void setCba(Double cba) {
		this.cba = cba;
	}
	public String getAttendList() {
		return attend_list;
	}
	public void setAttendList(String attendList) {
		attend_list = attendList;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
