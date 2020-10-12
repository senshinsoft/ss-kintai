package jp.co.senshinsoft.web;

import java.io.Serializable;

public class KinmuHokokushoForm implements Serializable{
// serialVersionUIDを指定
 private static final long serialVersionUID = 1L;

 //---------------共通-------------------
private String userId;
//-------------------------------------
//-----------userテーブル----------------
private String sei;
private String mei;
//-------------------------------------
//------work_report_dailyテーブル--------
private String day;
private String ssJkn;
private String tsJkn;
private String kkJkn;
private String kdJkn;
private String jkngi;
private String biko;
//-------------------------------------
//-----work_report_monthlyテーブル-------
private String year;
private String month;
private String teiji;
private String kdJknKei;
private String jkngiKei;
private String pjMei;
private String tokkijiko;
//-------------------------------------
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
}
