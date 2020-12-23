package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.senshinsoft.domain.Supplier;
import jp.co.senshinsoft.persistence.SupplierMapper;

@Service
public class SupplierService {

	@Autowired
	SupplierMapper mapper;
	
	/**
	 * 取引先コードと取引先名称を一覧で取得する
	 * @return
	 */
	public List<Supplier> supplierCatalog(){
		return mapper.supplierCatalog();
	}
}
