package jp.co.senshinsoft.web;

import java.util.List;

import jp.co.senshinsoft.domain.User;

public class KK03001Form {

	private String userId;
	private String sei;
	private String mei;
	private String year;
	private String month;
	private String employeeName;
	private List<User> empInfoList;
	private String comma;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSei() {
		return sei;
	}

	public void setSei(String sei) {
		this.sei = sei;
	}

	public String getMei() {
		return mei;
	}

	public void setMei(String mei) {
		this.mei = mei;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public List<User> getEmpInfoList() {
		return empInfoList;
	}

	public void setEmpInfoList(List<User> empInfoList) {
		this.empInfoList = empInfoList;
	}

	public String getComma() {
		comma=".　";
		return comma;
	}

	
}
