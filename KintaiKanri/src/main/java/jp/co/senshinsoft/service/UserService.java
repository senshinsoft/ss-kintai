package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.persistence.UserMapper;

@Service
public class UserService {
	
	@Autowired
	public UserMapper mapper;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/**
	 * mapperを呼び出して、ログインしている社員の名前(姓と名)をリストで取得する。
	 * テーブル名：user
	 * @param userId ログインしている社員の社員ID
	 * @return ログインしている社員の名前
	 */
	public String findEmployeeName(String userId){
		String userName = "";
		List<User> list = mapper.findEmployeeName(userId);
		for(User s : list) {
			userName+=s.getSei();
			userName+=" ";
			userName+=s.getMei();
		}
		return userName;
	}
	/**
	 * mapperを呼び出して、社員番号に該当するユーザ情報を取得する。
	 * @param userId
	 * @return 社員番号に該当するユーザ情報
	 */
	public User findAccountByUserId(String userId) {
		User user = mapper.findAccountByUserId(userId);
		return user;
	}
	/**
	 * mapperを呼び出して、社員一覧を取得する。
	 * @return
	 */
	public List<User> findEmployeeCatalog(){
		return mapper.findEmployeeCatalog();
	}
	
	/**
	 * mapperを呼び出して、社員の姓と名からユーザーIDを取得する
	 * @param sei
	 * @param mei
	 * @return
	 */
	public String findEmployeeUserId(String sei,String mei) {
		return mapper.findSelectEmployeeId(sei, mei);
	}
	/**
	 * mapperを呼び出してパスワードをハッシュ化してから更新を行う
	 * @param user
	 */
	public void updateUserPass(User user) {
		 mapper.updatePassword(user);
	}
	
	
	// ユーザーの重複チェック
	public Boolean searchUser(User user) {
		int checkCount = mapper.searchUser(user);
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
	public void registeringUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		mapper.registeringUser(user);
	}
	
	public List<User> findUser(String userId){
		return mapper.findUser(userId);
	}
	
	public void updateUser(User user) {
		mapper.updateUser(user);
	}
	
	public List<String> findMailAddress() {
		return mapper.findMailAddress();
	}
}
