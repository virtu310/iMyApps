package net.imyapps.common;

import java.io.Serializable;

public class CountAmount implements Serializable {
	private static final long serialVersionUID = 1607095163305722822L;
	
	Integer count;
	Integer amount;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
}
