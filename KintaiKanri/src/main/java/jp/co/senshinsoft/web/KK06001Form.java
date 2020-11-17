package jp.co.senshinsoft.web;

import java.io.Serializable;

public class KK06001Form implements Serializable {
	// serialVersionUIDを指定
	private static final long serialVersionUID = 1L;
	private String password;
	private String newPassword;
	private String confPassword;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfPassword() {
		return confPassword;
	}
	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}
}
