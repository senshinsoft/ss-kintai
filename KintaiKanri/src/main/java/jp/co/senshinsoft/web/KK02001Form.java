package jp.co.senshinsoft.web;

import java.util.List;

public class KK02001Form {

	private String year;
	private List<String> workCalendar;
	private String month;
	
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public List<String> getWorkCalendar() {
		return workCalendar;
	}
	
	public void setWorkCalendar(List<String> workCalendar) {
		this.workCalendar = workCalendar;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}
