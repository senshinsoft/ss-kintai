package jp.co.senshinsoft.web;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class KK04001Form implements Serializable {
	// serialVersionUIDを指定
	private static final long serialVersionUID = 1L;
	// --------------userテーブル--------------
	private String userId;
	private String sei;
	private String mei;
	// ---------------------------------------
	// --------------work_report_dailyテーブル--------------
	private String day;
	private String ssJkn;
	private String tsJkn;
	private String kkJkn;
	private String kdJkn;
	private String jkngi;
	private String biko;
	// ---------------------------------------------------
	// --------------work_report_monthlyテーブル--------------
	private String year;
	private String month;
	private String teiji;
	private String kdJknKei;
	private String jkngiKei;
	private String pjMei;
	private String tokkijiko;
	// ---------------------------------------------------
	// --------------該当月の1ヶ月のカレンダー----------------
	private List<String> targetMonth;
	private List<String> targetMonthDayOfTheWeek;
	// ---------------------------------------------------

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

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
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

	public List<String> getTargetMonth() {
		List<String> dateList = new ArrayList<>();
		List<String> dayOfTheWeekList = new ArrayList<>();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd　E");
		calendar.setTime(date);
		// 該当月の最後の日付の取得
		int last = calendar.getActualMaximum(Calendar.DATE);
		// 該当月の最初の日付を取得
		int first = calendar.getActualMinimum(Calendar.DATE);
		
		//該当月の初日から月末日までを取得して、リストに加える。
		do {
			calendar.set(Calendar.DATE, first);
			dateList.add(sdf.format(calendar.getTime()).substring(8, 12));
			//dateList.add(sdf.format(calendar.getTime()).substring(11, 12));
			first++;
		} while (first <= last);
		
		//曜日リストに該当月の曜日を加える
		setTargetMonthDayOfTheWeek(dayOfTheWeekList);
		return dateList;
	}

	public List<String> getTargetMonthDayOfTheWeek() {

		return targetMonthDayOfTheWeek;
	}

	public void setTargetMonthDayOfTheWeek(List<String> targetMonthDayOfTheWeek) {
		this.targetMonthDayOfTheWeek = targetMonthDayOfTheWeek;
	}
}
