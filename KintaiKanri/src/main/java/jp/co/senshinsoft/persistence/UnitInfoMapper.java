package jp.co.senshinsoft.persistence;

import java.util.List;

import jp.co.senshinsoft.domain.UnitInfo;

public interface UnitInfoMapper {

	public void registUnitInfo(UnitInfo unitInfo);
	
	public List<UnitInfo> findUnitInfo(String userId);
	
	public int updateUnitInfo(UnitInfo unitInfo);
}
