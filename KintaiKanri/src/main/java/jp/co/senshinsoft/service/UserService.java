package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.persistence.UserMapper;

@Service
public class UserService {
	
	@Autowired
	public UserMapper mapper;
	
	/**
	 * mapperを呼び出して、ログインしている社員の名前(姓と名)をリストで取得する。
	 * テーブル名：user
	 * @param userId ログインしている社員の社員ID
	 */
	public void findEmployeeName(String userId){
		List<User>  userSeiMeiList = mapper.findEmployeeName(userId);
	}
}
