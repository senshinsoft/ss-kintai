package jp.co.senshinsoft.web;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class KK01001Form implements Serializable {
	// serialVersionUIDを指定
	private static final long serialVersionUID = 1L;

	@NotEmpty(message="{userId_001}")
	@Size(min=1, max=4, message="{userId_002}")
	private String userId;

	@NotEmpty(message="{password_001}")
	@Size(min=8, max=20, message="{password_002}")
	private String password;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
