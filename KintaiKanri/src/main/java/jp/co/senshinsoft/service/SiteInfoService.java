package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.senshinsoft.domain.SiteInfo;
import jp.co.senshinsoft.persistence.SiteInfoMapper;

@Service
public class SiteInfoService {

	@Autowired
	SiteInfoMapper mapper;
	
	/**
	 * ユーザー登録画面(KK06002)で入力されたサイト情報を登録する
	 * @param siteInfo
	 */
	public void registSiteInfo(SiteInfo siteInfo) {
		mapper.registSiteInfo(siteInfo);
	}
	
	/**
	 * ユーザーIDに該当するサイト情報を取得する
	 * @param userId
	 * @return 該当ユーザーのサイト情報
	 */
	public List<SiteInfo> findSiteInfo(String userId){
		return mapper.findSiteInfo(userId);
	}
	
	/**
	 * ユーザー登録画面(KK06002)で入力されたサイト情報を更新する
	 * @param siteInfo
	 */
	public int updateSiteInfo(SiteInfo siteInfo) {
		return mapper.updateSiteInfo(siteInfo);
	}
}
