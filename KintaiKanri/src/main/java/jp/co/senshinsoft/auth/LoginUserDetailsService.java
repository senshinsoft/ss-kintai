package jp.co.senshinsoft.auth;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.persistence.UserMapper;

@Service
public class LoginUserDetailsService implements UserDetailsService {
	@Autowired
	UserMapper userMapper;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		// 社員番号に該当したユーザ情報を取得する
		User user = Optional.ofNullable(userMapper.findAccountByUserId(userId))
							.orElseThrow(() -> new UsernameNotFoundException("userId is not found."));
		
		return new LoginUserDetails(user, getAuthorities(user));		
	}

	// ユーザが保持する権限（ロール）を取得する
	private Collection<GrantedAuthority> getAuthorities(User user) {
		// ユーザが管理者の場合
		if(user.getAdminFlg().equals("1")) {
			return AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
		// ユーザが一般社員の場合
		} else {
			return AuthorityUtils.createAuthorityList("ROLE_USER");
		}
	}
	
}
