package jp.co.senshinsoft.persistence;

import jp.co.senshinsoft.domain.RegisteringUser;

public interface RegisteringUserMapper {
	
	
	public void registeringUser(RegisteringUser registeringUsr);
	

	public int searchUser(RegisteringUser registeringUsr);
}
