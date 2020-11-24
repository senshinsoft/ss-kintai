package jp.co.senshinsoft.web;

import java.io.Serializable;

import javax.validation.constraints.Size;

public class KK06002Form implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String mailAddress;
	private String password;
	private String sei;
	private String mei;
	private String adminFlg;
	private String regist;
	private String checkRepeat;
	private String updateFlg;
	private String searchEmpId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getAdminFlg() {
		return adminFlg;
	}

	public void setAdminFlg(String adminFlg) {
		this.adminFlg = adminFlg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUpdateFlg() {
		return updateFlg;
	}

	public void setUpdateFlg(String updateFlg) {
		this.updateFlg = updateFlg;
	}

	public String getCheckRepeat() {
		return checkRepeat;
	}

	public void setCheckRepeat(String checkRepeat) {
		this.checkRepeat = checkRepeat;
	}

	public String getRegist() {
		return regist;
	}

	public void setRegist(String regist) {
		this.regist = regist;
	}

	public String getSearchEmpId() {
		return searchEmpId;
	}

	public void setSearchEmpId(String searchEmpId) {
		this.searchEmpId = searchEmpId;
	}
}
