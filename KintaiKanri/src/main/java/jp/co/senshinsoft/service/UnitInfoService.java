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
	
	/**
	 * ユーザー登録画面(KK06002)で入力されたユニット情報を登録する
	 * @param unitInfo
	 */
	public void registUnitInfo(UnitInfo unitInfo) {
		mapper.registUnitInfo(unitInfo);
		}
	
	/**
	 * ユーザーIDに該当するユニット情報を取得する
	 * @param userId
	 * @return 該当ユーザーのユニット情報
	 */
	public List<UnitInfo> findUnitInfo(String userId){
		return mapper.findUnitInfo(userId);
	}
	
	/**
	 * ユーザー登録画面(KK06002)で入力されたユニット情報を更新する
	 * @param unitInfo
	 */
	public int updateSiteInfo(UnitInfo unitInfo) {
		return mapper.updateUnitInfo(unitInfo);
	}
}
