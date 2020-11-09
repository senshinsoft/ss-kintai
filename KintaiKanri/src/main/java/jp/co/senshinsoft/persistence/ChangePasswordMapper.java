package jp.co.senshinsoft.persistence;

import jp.co.senshinsoft.domain.User;

public interface ChangePasswordMapper {

	
	
	/**KK06001に入力された新しいパスワードをDBに登録
	 * 登録テーブル：user
	 * 
	 */
	public void updateUserPassword(User user);
}
