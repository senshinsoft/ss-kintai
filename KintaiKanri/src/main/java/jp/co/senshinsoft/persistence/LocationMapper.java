package jp.co.senshinsoft.persistence;

import java.util.List;

import jp.co.senshinsoft.domain.Location;

public interface LocationMapper {

	public List<Location> findLocationInfo(String locationCode);
}
