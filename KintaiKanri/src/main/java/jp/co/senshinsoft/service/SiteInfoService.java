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
	
	
	public void registSiteInfo(SiteInfo siteInfo) {
		mapper.registSiteInfo(siteInfo);
	}
	
	public List<SiteInfo> findSiteInfo(String userId){
		return mapper.findSiteInfo(userId);
	}
	
	public int updateSiteInfo(SiteInfo siteInfo) {
		return mapper.updateSiteInfo(siteInfo);
	}
}
