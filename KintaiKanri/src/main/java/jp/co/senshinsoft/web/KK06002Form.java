package jp.co.senshinsoft.web;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.Size;

public class KK06002Form implements Serializable {

	private static final long serialVersionUID = 1L;

	//ユーザー登録に必要な値
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
	
	//初期表示に必要なMap
	private Map<String,String> locationMap;
	private Map<String,String> supplierMap;
	private Map<String,String> userMap;
	
	//ロケーション情報
	private String supplierCode;
	private String locationCode;
	private String teiji;
	private String teijiDecimal;
	private String ssJkn;
	private String tsJkn;
	private String kkJkn;
	
	//ユニット情報
	private String leaderEmpId;
	private String assignmentLocationUserId;
	

	
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

	public Map<String, String> getLocationMap() {
		return locationMap;
	}

	public void setLocationMap(Map<String, String> locationMap) {
		this.locationMap = locationMap;
	}

	public Map<String, String> getSupplierMap() {
		return supplierMap;
	}

	public void setSupplierMap(Map<String, String> supplierMap) {
		this.supplierMap = supplierMap;
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getTeiji() {
		return teiji;
	}

	public void setTeiji(String teiji) {
		this.teiji = teiji;
	}

	public String getTeijiDecimal() {
		return teijiDecimal;
	}

	public void setTeijiDecimal(String teijiDecimal) {
		this.teijiDecimal = teijiDecimal;
	}

	public String getSsJkn() {
		return ssJkn;
	}

	public void setSsJkn(String ssJkn) {
		this.ssJkn = ssJkn;
	}

	public String getTsJkn() {
		return tsJkn;
	}

	public void setTsJkn(String tsJkn) {
		this.tsJkn = tsJkn;
	}

	public String getKkJkn() {
		return kkJkn;
	}

	public void setKkJkn(String kkJkn) {
		this.kkJkn = kkJkn;
	}

	public String getLeaderEmpId() {
		return leaderEmpId;
	}

	public void setLeaderEmpId(String leaderEmpId) {
		this.leaderEmpId = leaderEmpId;
	}

	public String getAssignmentLocationUserId() {
		return assignmentLocationUserId;
	}

	public void setAssignmentLocationUserId(String assignmentLocationUserId) {
		this.assignmentLocationUserId = assignmentLocationUserId;
	}
}
