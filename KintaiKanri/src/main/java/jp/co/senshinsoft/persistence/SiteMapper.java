package jp.co.senshinsoft.persistence;

import java.util.List;

public interface SiteMapper {

	public List<String> findLocationList(String supplierCode);
}
