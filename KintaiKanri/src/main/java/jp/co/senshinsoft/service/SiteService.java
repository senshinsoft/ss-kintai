package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.senshinsoft.persistence.SiteMapper;

@Service
public class SiteService {

	@Autowired
	SiteMapper mapper;
	
	/**
	 * 取引先に該当するロケーションコードを取得する
	 * @param supplierCode
	 * @return
	 */
	public List<String> findLocatist(String supplierCode){
		return mapper.findLocationList(supplierCode);
	}
}
