package jp.co.senshinsoft.web;

import java.io.Serializable;
import java.util.List;

public class KK04001Form implements Serializable {
	// serialVersionUIDを指定
	private static final long serialVersionUID = 1L;
	// --------------userテーブル--------------
	private String userId;
	private String userName;
	private String adminFlg;
	private String useUserId;
	// ---------------------------------------
	// --------------work_report_dailyテーブル-------------------
	private String year;
	private String month;
	private String day;
	private String ssJkn;
	private String tsJkn;
	private String kkJkn;
	private String kdJkn;
	private String jkngi;
	private String biko;
	// ---------------------------------------------------
	// --------------work_report_monthlyテーブル--------------
	private String workingDate;
	private String teiji;
	private String kdJknKei;
	private String jkngiKei;
	private String pjMei;
	private String tokkijiko;
	private String authFlg;
	// ---------------------------------------------------
	// --------------該当月の1ヶ月のカレンダー----------------
	private List<String> targetMonthDayOfTheWeek;
	// ---------------------------------------------------

	public KK04001Form() {
		// --------------userテーブル--------------
		userId = "";
		userName = "";
		useUserId="";  //参照している勤務表のユーザーID
		// ---------------------------------------
		// --------------work_report_dailyテーブル-------------------
		ssJkn = "";
		tsJkn = "";
		kkJkn = "";
		kdJkn = "";
		jkngi = "";
		biko = "";
		// ---------------------------------------------------
		// --------------work_report_monthlyテーブル--------------
		workingDate = "";
		teiji = "";
		kdJknKei = "";
		jkngiKei = "";
		pjMei = "";
		tokkijiko = "";
		authFlg="";
		// ---------------------------------------------------
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getKdJkn() {
		return kdJkn;
	}

	public void setKdJkn(String kdJkn) {
		this.kdJkn = kdJkn;
	}

	public String getJkngi() {
		return jkngi;
	}

	public void setJkngi(String jkngi) {
		this.jkngi = jkngi;
	}

	public String getBiko() {
		return biko;
	}

	public void setBiko(String biko) {
		this.biko = biko;
	}

	public String getTeiji() {
		return teiji;
	}

	public void setTeiji(String teiji) {
		this.teiji = teiji;
	}

	public String getKdJknKei() {
		return kdJknKei;
	}

	public void setKdJknKei(String kdJknKei) {
		this.kdJknKei = kdJknKei;
	}

	public String getJkngiKei() {
		return jkngiKei;
	}

	public void setJkngiKei(String jkngiKei) {
		this.jkngiKei = jkngiKei;
	}

	public String getPjMei() {
		return pjMei;
	}

	public void setPjMei(String pjMei) {
		this.pjMei = pjMei;
	}

	public String getTokkijiko() {
		return tokkijiko;
	}

	public void setTokkijiko(String tokkijiko) {
		this.tokkijiko = tokkijiko;
	}

	public List<String> getTargetMonthDayOfTheWeek() {

		return targetMonthDayOfTheWeek;
	}

	public void setTargetMonthDayOfTheWeek(List<String> targetMonthDayOfTheWeek) {
		this.targetMonthDayOfTheWeek = targetMonthDayOfTheWeek;
	}

	public String getWorkingDate() {
		return workingDate;
	}

	public void setWorkingDate(String workingDate) {
		this.workingDate = workingDate;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
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

	public String getAuthFlg() {
		return authFlg;
	}

	public void setAuthFlg(String authFlg) {
		this.authFlg = authFlg;
	}

	public String getAdminFlg() {
		return adminFlg;
	}

	public void setAdminFlg(String adminFlg) {
		this.adminFlg = adminFlg;
	}

	public String getUseUserId() {
		return useUserId;
	}

	public void setUseUserId(String useUserId) {
		this.useUserId = useUserId;
	}
}
