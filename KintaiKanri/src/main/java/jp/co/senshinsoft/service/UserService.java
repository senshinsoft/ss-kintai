package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.mapper.UserMapper;

@Service
public class UserService {
	
	@Autowired
	public UserMapper mapper;
	/**
	 * mapperを呼び出して、ログインしている社員の名前(姓と名)をリストで返す
	 * テーブル名：user
	 * @param userId ログインしている社員の社員ID
	 * @return 姓(sei)、名(mei)
	 */
	public List<User> findEmployeeName(String userId){
		return mapper.findEmployeeName(userId);
	}
}
