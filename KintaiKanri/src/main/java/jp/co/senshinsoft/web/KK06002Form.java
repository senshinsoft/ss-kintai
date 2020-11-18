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
		private int adminFlg;

		private String regist;		
		private String checkRepeat;
		private String finalComment;
		
		
		public String getFinalComment() {
			return finalComment;
		}
		public void setFinalComment(String finalComment) {
			this.finalComment = finalComment;
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
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		
		

}
