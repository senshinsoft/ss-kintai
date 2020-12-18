package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.senshinsoft.domain.UnitInfo;
import jp.co.senshinsoft.persistence.UnitInfoMapper;

@Service
public class UnitInfoService {

	@Autowired
	UnitInfoMapper mapper;
	
	
	public void registUnitInfo(UnitInfo unitInfo) {
		mapper.registUnitInfo(unitInfo);
		}
	public List<UnitInfo> findUnitInfo(String userId){
		return mapper.findUnitInfo(userId);
	}
	public int updateSiteInfo(UnitInfo unitInfo) {
		return mapper.updateUnitInfo(unitInfo);
	}
}
