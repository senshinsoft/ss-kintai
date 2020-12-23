package jp.co.senshinsoft.domain;

public class Site {

	private String supplierCode;
	private String locationCode;
	private String beginDate;
	private String endDate;
	private String siteDeleted;
	
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
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getSiteDeleted() {
		return siteDeleted;
	}
	public void setSiteDeleted(String siteDeleted) {
		this.siteDeleted = siteDeleted;
	}
	
}
