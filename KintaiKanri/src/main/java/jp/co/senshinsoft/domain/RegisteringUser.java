package jp.co.senshinsoft.domain;

public class RegisteringUser {
	
	private String userId;
	private String mailAddress;
	private String password;
	private String sei;
	private String mei;
	private int adminFlg;
	private int RegisterCheck;	
	
	public int getRegisterCheck() {
		return RegisterCheck;
	}
	public void setRegisterCheck(int registerCheck) {
		RegisterCheck = registerCheck;
	}
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
	public int getAdminFlg() {
		return adminFlg;
	}
	public void setAdminFlg(int adminFlg) {
		this.adminFlg = adminFlg;
	}

}
