package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.senshinsoft.domain.Location;
import jp.co.senshinsoft.persistence.LocationMapper;

@Service
public class LocationService {
	
	@Autowired
	LocationMapper mapper;
	
	/**
	 * ロケーションコードとロケーション名をリストで返す
	 * @param locationCode
	 * @return
	 */
	public List<Location> findLocationInfo(String locationCode){
		return mapper.findLocationInfo(locationCode);
	}
}
