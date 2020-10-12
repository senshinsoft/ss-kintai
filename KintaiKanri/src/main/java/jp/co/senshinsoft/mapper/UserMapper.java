package jp.co.senshinsoft.mapper;

import java.util.List;

import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.domain.WorkReportDaily;

public interface UserMapper {
	/**
	 * @return 該当社員の姓と名
	 */
	public List<User> findEmployeeName(String userId);
}