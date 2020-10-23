package jp.co.senshinsoft.persistence;

import java.util.List;

import jp.co.senshinsoft.domain.User;

public interface UserMapper {
	/**
	 * ログインしている社員の名前(姓と名)をリストで返すインタフェース
	 * テーブル名：user
	 * @param userId ログインしている社員の社員ID
	 * @return 姓(sei)、名(mei)
	 */
	public List<User> findEmployeeName(String userId);
		
	/**
	 * 社員番号に該当するユーザ情報を取得するインタフェース
	 * @param userId 社員番号
	 * @return 社員番号に該当するユーザ情報
	 */
	public User findAccountByUserId(String userId);

}