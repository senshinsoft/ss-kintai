package jp.co.senshinsoft.web;

import javax.validation.constraints.NotEmpty;

public class KK06001Form {
	
	@NotEmpty
	private String old_pass;
	
	@NotEmpty
	private String new_pass;
	
	@NotEmpty
	private String check_pass;
	
	
	public String getOld_pass() {
		return old_pass;
	}
	public void setOld_pass(String old_pass) {
		this.old_pass = old_pass;
	}
	public String getNew_pass() {
		return new_pass;
	}
	public void setNew_pass(String new_pass) {
		this.new_pass = new_pass;
	}
	public String getCheck_pass() {
		return check_pass;
	}
	public void setCheck_pass(String check_pass) {
		this.check_pass = check_pass;
	}
	
	

}
