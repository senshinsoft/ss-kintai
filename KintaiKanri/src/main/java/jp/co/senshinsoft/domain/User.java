package jp.co.senshinsoft.domain;

public class User {
private String userId;
private String mailAddress;
private String password;
private String sei;
private String mei;
private String adminFlg;
private String insUser;
private String updUser;

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
public String getInsUser() {
	return insUser;
}
public void setInsUser(String insUser) {
	this.insUser = insUser;
}
public String getUpdUser() {
	return updUser;
}
public void setUpdUser(String updUser) {
	this.updUser = updUser;
}
}
