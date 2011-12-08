package net.imyapps.common;

import java.io.Serializable;
import java.util.Map;

public class Statistic implements Serializable {
	private static final long serialVersionUID = -2090539468277861523L;
	
	CountAmount total;
	CountAmount paid;
	Map<String, CountAmount> byGenre;
	
	public CountAmount getTotal() {
		return total;
	}
	public void setTotal(CountAmount total) {
		this.total = total;
	}
	public CountAmount getPaid() {
		return paid;
	}
	public void setPaid(CountAmount paid) {
		this.paid = paid;
	}
	public Map<String, CountAmount> getByGenre() {
		return byGenre;
	}
	public void setByGenre(Map<String, CountAmount> genre) {
		this.byGenre = genre;
	}
	
}
