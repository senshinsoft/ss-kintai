package jp.co.senshinsoft.persistence;

import java.util.List;

import jp.co.senshinsoft.domain.SiteInfo;

public interface SiteInfoMapper {
	
	public void registSiteInfo(SiteInfo siteInfo);
	
	public List<SiteInfo> findSiteInfo(String userId);
	
	public int updateSiteInfo(SiteInfo siteInfo);

}
