package jp.co.senshinsoft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.senshinsoft.domain.RegisteringUser;
import jp.co.senshinsoft.persistence.RegisteringUserMapper;

@Service
public class RegisteringUserService {
	
	@Autowired
	public RegisteringUserMapper mapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	// ユーザーの重複チェック
	public Boolean searchUser(RegisteringUser registeringUser) {
		int checkCount = mapper.searchUser(registeringUser);
		if(checkCount == 0) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * パスワードをハッシュ化して新規ユーザーを登録する
	 * テーブル名：user
	 * 
	 */
	public void registeringUser(RegisteringUser registeringUser) {
		registeringUser.setPassword(passwordEncoder.encode(registeringUser.getPassword()));
		
		mapper.registeringUser(registeringUser);
	}
}
